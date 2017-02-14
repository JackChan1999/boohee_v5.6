package butterknife.internal;

import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import butterknife.OnItemSelected;
import butterknife.OnLongClick;
import butterknife.OnPageChange;
import butterknife.OnTextChanged;
import butterknife.OnTouch;
import butterknife.Optional;
import butterknife.internal.ListenerClass.NONE;
import com.alipay.sdk.authjs.a;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;

public final class ButterKnifeProcessor extends AbstractProcessor {
    static final /* synthetic */ boolean $assertionsDisabled = (!ButterKnifeProcessor.class.desiredAssertionStatus());
    public static final String ANDROID_PREFIX = "android.";
    public static final String JAVA_PREFIX = "java.";
    private static final List<Class<? extends Annotation>> LISTENERS = Arrays.asList(new Class[]{OnCheckedChanged.class, OnClick.class, OnEditorAction.class, OnFocusChange.class, OnItemClick.class, OnItemLongClick.class, OnItemSelected.class, OnLongClick.class, OnPageChange.class, OnTextChanged.class, OnTouch.class});
    private static final String LIST_TYPE = List.class.getCanonicalName();
    public static final String SUFFIX = "$$ViewInjector";
    static final String VIEW_TYPE = "android.view.View";
    private Elements elementUtils;
    private Filer filer;
    private Types typeUtils;

    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        this.elementUtils = env.getElementUtils();
        this.typeUtils = env.getTypeUtils();
        this.filer = env.getFiler();
    }

    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportTypes = new LinkedHashSet();
        supportTypes.add(InjectView.class.getCanonicalName());
        supportTypes.add(InjectViews.class.getCanonicalName());
        for (Class<? extends Annotation> listener : LISTENERS) {
            supportTypes.add(listener.getCanonicalName());
        }
        return supportTypes;
    }

    public boolean process(Set<? extends TypeElement> set, RoundEnvironment env) {
        for (Entry<TypeElement, ViewInjector> entry : findAndParseTargets(env).entrySet()) {
            TypeElement typeElement = (TypeElement) entry.getKey();
            ViewInjector viewInjector = (ViewInjector) entry.getValue();
            try {
                Writer writer = this.filer.createSourceFile(viewInjector.getFqcn(), new Element[]{typeElement}).openWriter();
                writer.write(viewInjector.brewJava());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                error(typeElement, "Unable to write injector for type %s: %s", typeElement, e.getMessage());
            }
        }
        return true;
    }

    private Map<TypeElement, ViewInjector> findAndParseTargets(RoundEnvironment env) {
        Map<TypeElement, ViewInjector> targetClassMap = new LinkedHashMap();
        Set<String> erasedTargetNames = new LinkedHashSet();
        for (Element element : env.getElementsAnnotatedWith(InjectView.class)) {
            try {
                parseInjectView(element, targetClassMap, erasedTargetNames);
            } catch (Exception e) {
                e.printStackTrace(new PrintWriter(new StringWriter()));
                error(element, "Unable to generate view injector for @InjectView.\n\n%s", stackTrace);
            }
        }
        for (Element element2 : env.getElementsAnnotatedWith(InjectViews.class)) {
            try {
                parseInjectViews(element2, targetClassMap, erasedTargetNames);
            } catch (Exception e2) {
                e2.printStackTrace(new PrintWriter(new StringWriter()));
                error(element2, "Unable to generate view injector for @InjectViews.\n\n%s", stackTrace);
            }
        }
        for (Class<? extends Annotation> listener : LISTENERS) {
            findAndParseListener(env, listener, targetClassMap, erasedTargetNames);
        }
        for (Entry<TypeElement, ViewInjector> entry : targetClassMap.entrySet()) {
            String parentClassFqcn = findParentFqcn((TypeElement) entry.getKey(), erasedTargetNames);
            if (parentClassFqcn != null) {
                ((ViewInjector) entry.getValue()).setParentInjector(parentClassFqcn + SUFFIX);
            }
        }
        return targetClassMap;
    }

    private boolean isInaccessibleViaGeneratedCode(Class<? extends Annotation> annotationClass, String targetThing, Element element) {
        boolean hasError = false;
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
        Set<Modifier> modifiers = element.getModifiers();
        if (modifiers.contains(Modifier.PRIVATE) || modifiers.contains(Modifier.STATIC)) {
            error(element, "@%s %s must not be private or static. (%s.%s)", annotationClass.getSimpleName(), targetThing, enclosingElement.getQualifiedName(), element.getSimpleName());
            hasError = true;
        }
        if (enclosingElement.getKind() != ElementKind.CLASS) {
            error(enclosingElement, "@%s %s may only be contained in classes. (%s.%s)", annotationClass.getSimpleName(), targetThing, enclosingElement.getQualifiedName(), element.getSimpleName());
            hasError = true;
        }
        if (!enclosingElement.getModifiers().contains(Modifier.PRIVATE)) {
            return hasError;
        }
        error(enclosingElement, "@%s %s may not be contained in private classes. (%s.%s)", annotationClass.getSimpleName(), targetThing, enclosingElement.getQualifiedName(), element.getSimpleName());
        return true;
    }

    private boolean isBindingInWrongPackage(Class<? extends Annotation> annotationClass, Element element) {
        String qualifiedName = ((TypeElement) element.getEnclosingElement()).getQualifiedName().toString();
        if (qualifiedName.startsWith(ANDROID_PREFIX)) {
            error(element, "@%s-annotated class incorrectly in Android framework package. (%s)", annotationClass.getSimpleName(), qualifiedName);
            return true;
        } else if (!qualifiedName.startsWith(JAVA_PREFIX)) {
            return false;
        } else {
            error(element, "@%s-annotated class incorrectly in Java framework package. (%s)", annotationClass.getSimpleName(), qualifiedName);
            return true;
        }
    }

    private void parseInjectView(Element element, Map<TypeElement, ViewInjector> targetClassMap, Set<String> erasedTargetNames) {
        boolean hasError = false;
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
        TypeMirror elementType = element.asType();
        if (elementType instanceof TypeVariable) {
            elementType = ((TypeVariable) elementType).getUpperBound();
        }
        if (!(isSubtypeOfType(elementType, VIEW_TYPE) || isInterface(elementType))) {
            error(element, "@InjectView fields must extend from View or be an interface. (%s.%s)", enclosingElement.getQualifiedName(), element.getSimpleName());
            hasError = true;
        }
        hasError = (hasError | isInaccessibleViaGeneratedCode(InjectView.class, "fields", element)) | isBindingInWrongPackage(InjectView.class, element);
        if (element.getAnnotation(InjectViews.class) != null) {
            error(element, "Only one of @InjectView and @InjectViews is allowed. (%s.%s)", enclosingElement.getQualifiedName(), element.getSimpleName());
            hasError = true;
        }
        if (!hasError) {
            int id = ((InjectView) element.getAnnotation(InjectView.class)).value();
            ViewInjector injector = (ViewInjector) targetClassMap.get(enclosingElement);
            if (injector != null) {
                ViewInjection viewInjection = injector.getViewInjection(id);
                if (viewInjection != null) {
                    Iterator<ViewBinding> iterator = viewInjection.getViewBindings().iterator();
                    if (iterator.hasNext()) {
                        ViewBinding existingBinding = (ViewBinding) iterator.next();
                        error(element, "Attempt to use @InjectView for an already injected ID %d on '%s'. (%s.%s)", Integer.valueOf(id), existingBinding.getName(), enclosingElement.getQualifiedName(), element.getSimpleName());
                        return;
                    }
                }
            }
            getOrCreateTargetClass(targetClassMap, enclosingElement).addView(id, new ViewBinding(element.getSimpleName().toString(), elementType.toString(), element.getAnnotation(Optional.class) == null));
            erasedTargetNames.add(enclosingElement.toString());
        }
    }

    private void parseInjectViews(Element element, Map<TypeElement, ViewInjector> targetClassMap, Set<String> erasedTargetNames) {
        boolean hasError = false;
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
        TypeMirror elementType = element.asType();
        String erasedType = doubleErasure(elementType);
        TypeMirror viewType = null;
        Kind kind = null;
        if (elementType.getKind() == TypeKind.ARRAY) {
            viewType = ((ArrayType) elementType).getComponentType();
            kind = Kind.ARRAY;
        } else if (LIST_TYPE.equals(erasedType)) {
            List<? extends TypeMirror> typeArguments = ((DeclaredType) elementType).getTypeArguments();
            if (typeArguments.size() != 1) {
                error(element, "@InjectViews List must have a generic component. (%s.%s)", enclosingElement.getQualifiedName(), element.getSimpleName());
                hasError = true;
            } else {
                viewType = (TypeMirror) typeArguments.get(0);
            }
            kind = Kind.LIST;
        } else {
            error(element, "@InjectViews must be a List or array. (%s.%s)", enclosingElement.getQualifiedName(), element.getSimpleName());
            hasError = true;
        }
        if (viewType instanceof TypeVariable) {
            viewType = ((TypeVariable) viewType).getUpperBound();
        }
        if (!(viewType == null || isSubtypeOfType(viewType, VIEW_TYPE) || isInterface(viewType))) {
            error(element, "@InjectViews type must extend from View or be an interface. (%s.%s)", enclosingElement.getQualifiedName(), element.getSimpleName());
            hasError = true;
        }
        if (!((hasError | isInaccessibleViaGeneratedCode(InjectViews.class, "fields", element)) | isBindingInWrongPackage(InjectViews.class, element))) {
            String name = element.getSimpleName().toString();
            int[] ids = ((InjectViews) element.getAnnotation(InjectViews.class)).value();
            if (ids.length == 0) {
                error(element, "@InjectViews must specify at least one ID. (%s.%s)", enclosingElement.getQualifiedName(), element.getSimpleName());
                return;
            }
            if (findDuplicate(ids) != null) {
                error(element, "@InjectViews annotation contains duplicate ID %d. (%s.%s)", findDuplicate(ids), enclosingElement.getQualifiedName(), element.getSimpleName());
            }
            if ($assertionsDisabled || viewType != null) {
                getOrCreateTargetClass(targetClassMap, enclosingElement).addCollection(ids, new CollectionBinding(name, viewType.toString(), kind, element.getAnnotation(Optional.class) == null));
                erasedTargetNames.add(enclosingElement.toString());
                return;
            }
            throw new AssertionError();
        }
    }

    private static Integer findDuplicate(int[] array) {
        Set<Integer> seenElements = new LinkedHashSet();
        for (int element : array) {
            if (!seenElements.add(Integer.valueOf(element))) {
                return Integer.valueOf(element);
            }
        }
        return null;
    }

    private String doubleErasure(TypeMirror elementType) {
        String name = this.typeUtils.erasure(elementType).toString();
        int typeParamStart = name.indexOf(60);
        if (typeParamStart != -1) {
            return name.substring(0, typeParamStart);
        }
        return name;
    }

    private void findAndParseListener(RoundEnvironment env, Class<? extends Annotation> annotationClass, Map<TypeElement, ViewInjector> targetClassMap, Set<String> erasedTargetNames) {
        for (Element element : env.getElementsAnnotatedWith(annotationClass)) {
            try {
                parseListenerAnnotation(annotationClass, element, targetClassMap, erasedTargetNames);
            } catch (Exception e) {
                e.printStackTrace(new PrintWriter(new StringWriter()));
                error(element, "Unable to generate view injector for @%s.\n\n%s", annotationClass.getSimpleName(), stackTrace.toString());
            }
        }
    }

    private void parseListenerAnnotation(Class<? extends Annotation> annotationClass, Element element, Map<TypeElement, ViewInjector> targetClassMap, Set<String> erasedTargetNames) throws Exception {
        if ((element instanceof ExecutableElement) && element.getKind() == ElementKind.METHOD) {
            ExecutableElement executableElement = (ExecutableElement) element;
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            Annotation annotation = element.getAnnotation(annotationClass);
            Method annotationValue = annotationClass.getDeclaredMethod("value", new Class[0]);
            if (annotationValue.getReturnType() != int[].class) {
                throw new IllegalStateException(String.format("@%s annotation value() type not int[].", new Object[]{annotationClass}));
            }
            int[] ids = (int[]) annotationValue.invoke(annotation, new Object[0]);
            String name = executableElement.getSimpleName().toString();
            boolean required = element.getAnnotation(Optional.class) == null;
            boolean hasError = isInaccessibleViaGeneratedCode(annotationClass, "methods", element) | isBindingInWrongPackage(annotationClass, element);
            if (findDuplicate(ids) != null) {
                error(element, "@%s annotation for method contains duplicate ID %d. (%s.%s)", annotationClass.getSimpleName(), findDuplicate(ids), enclosingElement.getQualifiedName(), element.getSimpleName());
                hasError = true;
            }
            ListenerClass listener = (ListenerClass) annotationClass.getAnnotation(ListenerClass.class);
            if (listener == null) {
                throw new IllegalStateException(String.format("No @%s defined on @%s.", new Object[]{ListenerClass.class.getSimpleName(), annotationClass.getSimpleName()}));
            }
            int i;
            int length;
            for (int id : ids) {
                if (id == -1) {
                    if (ids.length == 1) {
                        if (!required) {
                            error(element, "ID free injection must not be annotated with @Optional. (%s.%s)", enclosingElement.getQualifiedName(), element.getSimpleName());
                            hasError = true;
                        }
                        if (!(isSubtypeOfType(enclosingElement.asType(), listener.targetType()) || isInterface(enclosingElement.asType()))) {
                            error(element, "@%s annotation without an ID may only be used with an object of type \"%s\" or an interface. (%s.%s)", annotationClass.getSimpleName(), targetType, enclosingElement.getQualifiedName(), element.getSimpleName());
                            hasError = true;
                        }
                    } else {
                        error(element, "@%s annotation contains invalid ID %d. (%s.%s)", annotationClass.getSimpleName(), Integer.valueOf(id), enclosingElement.getQualifiedName(), element.getSimpleName());
                        hasError = true;
                    }
                }
            }
            ListenerMethod[] methods = listener.method();
            if (methods.length > 1) {
                throw new IllegalStateException(String.format("Multiple listener methods specified on @%s.", new Object[]{annotationClass.getSimpleName()}));
            }
            ListenerMethod method;
            if (methods.length != 1) {
                Enum<?> callback = (Enum) annotationClass.getDeclaredMethod(a.c, new Class[0]).invoke(annotation, new Object[0]);
                method = (ListenerMethod) callback.getDeclaringClass().getField(callback.name()).getAnnotation(ListenerMethod.class);
                if (method == null) {
                    throw new IllegalStateException(String.format("No @%s defined on @%s's %s.%s.", new Object[]{ListenerMethod.class.getSimpleName(), annotationClass.getSimpleName(), callback.getDeclaringClass().getSimpleName(), callback.name()}));
                }
            } else if (listener.callbacks() != NONE.class) {
                throw new IllegalStateException(String.format("Both method() and callback() defined on @%s.", new Object[]{annotationClass.getSimpleName()}));
            } else {
                method = methods[0];
            }
            List<? extends VariableElement> methodParameters = executableElement.getParameters();
            if (methodParameters.size() > method.parameters().length) {
                error(element, "@%s methods can have at most %s parameter(s). (%s.%s)", annotationClass.getSimpleName(), Integer.valueOf(method.parameters().length), enclosingElement.getQualifiedName(), element.getSimpleName());
                hasError = true;
            }
            TypeMirror returnType = executableElement.getReturnType();
            if (returnType instanceof TypeVariable) {
                returnType = ((TypeVariable) returnType).getUpperBound();
            }
            if (!returnType.toString().equals(method.returnType())) {
                error(element, "@%s methods must have a '%s' return type. (%s.%s)", annotationClass.getSimpleName(), method.returnType(), enclosingElement.getQualifiedName(), element.getSimpleName());
                hasError = true;
            }
            if (!hasError) {
                Parameter[] parameters = Parameter.NONE;
                if (!methodParameters.isEmpty()) {
                    parameters = new Parameter[methodParameters.size()];
                    BitSet bitSet = new BitSet(methodParameters.size());
                    String[] parameterTypes = method.parameters();
                    for (int i2 = 0; i2 < methodParameters.size(); i2++) {
                        TypeMirror methodParameterType = ((VariableElement) methodParameters.get(i2)).asType();
                        if (methodParameterType instanceof TypeVariable) {
                            methodParameterType = ((TypeVariable) methodParameterType).getUpperBound();
                        }
                        int j = 0;
                        while (j < parameterTypes.length) {
                            if (!bitSet.get(j) && (isSubtypeOfType(methodParameterType, parameterTypes[j]) || isInterface(methodParameterType))) {
                                parameters[i2] = new Parameter(j, methodParameterType.toString());
                                bitSet.set(j);
                                break;
                            }
                            j++;
                        }
                        if (parameters[i2] == null) {
                            StringBuilder builder = new StringBuilder();
                            builder.append("Unable to match @").append(annotationClass.getSimpleName()).append(" method arguments. (").append(enclosingElement.getQualifiedName()).append('.').append(element.getSimpleName()).append(')');
                            for (j = 0; j < parameters.length; j++) {
                                Parameter parameter = parameters[j];
                                builder.append("\n\n  Parameter #").append(j + 1).append(": ").append(((VariableElement) methodParameters.get(j)).asType().toString()).append("\n    ");
                                if (parameter == null) {
                                    builder.append("did not match any listener parameters");
                                } else {
                                    builder.append("matched listener parameter #").append(parameter.getListenerPosition() + 1).append(": ").append(parameter.getType());
                                }
                            }
                            builder.append("\n\nMethods may have up to ").append(method.parameters().length).append(" parameter(s):\n");
                            for (String parameterType : method.parameters()) {
                                builder.append("\n  ").append(parameterType);
                            }
                            builder.append("\n\nThese may be listed in any order but will be searched for from top to bottom.");
                            error(executableElement, builder.toString(), new Object[0]);
                            return;
                        }
                    }
                }
                ListenerBinding binding = new ListenerBinding(name, Arrays.asList(parameters), required);
                ViewInjector viewInjector = getOrCreateTargetClass(targetClassMap, enclosingElement);
                length = ids.length;
                i = 0;
                while (i < length) {
                    if (viewInjector.addListener(ids[i], listener, method, binding)) {
                        i++;
                    } else {
                        error(element, "Multiple listener methods with return value specified for ID %d. (%s.%s)", Integer.valueOf(ids[i]), enclosingElement.getQualifiedName(), element.getSimpleName());
                        return;
                    }
                }
                erasedTargetNames.add(enclosingElement.toString());
                return;
            }
            return;
        }
        throw new IllegalStateException(String.format("@%s annotation must be on a method.", new Object[]{annotationClass.getSimpleName()}));
    }

    private boolean isInterface(TypeMirror typeMirror) {
        if ((typeMirror instanceof DeclaredType) && ((DeclaredType) typeMirror).asElement().getKind() == ElementKind.INTERFACE) {
            return true;
        }
        return false;
    }

    private boolean isSubtypeOfType(TypeMirror typeMirror, String otherType) {
        if (otherType.equals(typeMirror.toString())) {
            return true;
        }
        if (!(typeMirror instanceof DeclaredType)) {
            return false;
        }
        DeclaredType declaredType = (DeclaredType) typeMirror;
        List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
        if (typeArguments.size() > 0) {
            StringBuilder typeString = new StringBuilder(declaredType.asElement().toString());
            typeString.append('<');
            for (int i = 0; i < typeArguments.size(); i++) {
                if (i > 0) {
                    typeString.append(',');
                }
                typeString.append('?');
            }
            typeString.append('>');
            if (typeString.toString().equals(otherType)) {
                return true;
            }
        }
        Element element = declaredType.asElement();
        if (!(element instanceof TypeElement)) {
            return false;
        }
        TypeElement typeElement = (TypeElement) element;
        if (isSubtypeOfType(typeElement.getSuperclass(), otherType)) {
            return true;
        }
        for (TypeMirror interfaceType : typeElement.getInterfaces()) {
            if (isSubtypeOfType(interfaceType, otherType)) {
                return true;
            }
        }
        return false;
    }

    private ViewInjector getOrCreateTargetClass(Map<TypeElement, ViewInjector> targetClassMap, TypeElement enclosingElement) {
        ViewInjector viewInjector = (ViewInjector) targetClassMap.get(enclosingElement);
        if (viewInjector != null) {
            return viewInjector;
        }
        String targetType = enclosingElement.getQualifiedName().toString();
        String classPackage = getPackageName(enclosingElement);
        viewInjector = new ViewInjector(classPackage, getClassName(enclosingElement, classPackage) + SUFFIX, targetType);
        targetClassMap.put(enclosingElement, viewInjector);
        return viewInjector;
    }

    private static String getClassName(TypeElement type, String packageName) {
        return type.getQualifiedName().toString().substring(packageName.length() + 1).replace('.', '$');
    }

    private String findParentFqcn(TypeElement typeElement, Set<String> parents) {
        do {
            TypeMirror type = typeElement.getSuperclass();
            if (type.getKind() == TypeKind.NONE) {
                return null;
            }
            typeElement = (TypeElement) ((DeclaredType) type).asElement();
        } while (!parents.contains(typeElement.toString()));
        String packageName = getPackageName(typeElement);
        return packageName + "." + getClassName(typeElement, packageName);
    }

    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private void error(Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        this.processingEnv.getMessager().printMessage(Kind.ERROR, message, element);
    }

    private String getPackageName(TypeElement type) {
        return this.elementUtils.getPackageOf(type).getQualifiedName().toString();
    }
}
