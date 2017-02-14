package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.asm.ASMException;
import com.alibaba.fastjson.asm.ClassWriter;
import com.alibaba.fastjson.asm.Label;
import com.alibaba.fastjson.asm.MethodVisitor;
import com.alibaba.fastjson.asm.Opcodes;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.ASMClassLoader;
import com.alibaba.fastjson.util.ASMUtils;
import com.alibaba.fastjson.util.DeserializeBeanInfo;
import com.alibaba.fastjson.util.FieldInfo;
import com.alipay.sdk.sys.a;
import com.boohee.one.ui.adapter.PostPicturePreviewAdapter;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;

public class ASMDeserializerFactory implements Opcodes {
    private static final ASMDeserializerFactory instance = new ASMDeserializerFactory();
    private final ASMClassLoader classLoader;
    private final AtomicLong seed;

    static class Context {
        private final DeserializeBeanInfo beanInfo;
        private String className;
        private Class<?> clazz;
        private List<FieldInfo> fieldInfoList;
        private int variantIndex = 5;
        private Map<String, Integer> variants = new HashMap();

        public Context(String className, ParserConfig config, DeserializeBeanInfo beanInfo, int initVariantIndex) {
            this.className = className;
            this.clazz = beanInfo.getClazz();
            this.variantIndex = initVariantIndex;
            this.beanInfo = beanInfo;
            this.fieldInfoList = new ArrayList(beanInfo.getFieldList());
        }

        public String getClassName() {
            return this.className;
        }

        public List<FieldInfo> getFieldInfoList() {
            return this.fieldInfoList;
        }

        public DeserializeBeanInfo getBeanInfo() {
            return this.beanInfo;
        }

        public Class<?> getClazz() {
            return this.clazz;
        }

        public int getVariantCount() {
            return this.variantIndex;
        }

        public int var(String name, int increment) {
            if (((Integer) this.variants.get(name)) == null) {
                this.variants.put(name, Integer.valueOf(this.variantIndex));
                this.variantIndex += increment;
            }
            return ((Integer) this.variants.get(name)).intValue();
        }

        public int var(String name) {
            if (((Integer) this.variants.get(name)) == null) {
                Map map = this.variants;
                int i = this.variantIndex;
                this.variantIndex = i + 1;
                map.put(name, Integer.valueOf(i));
            }
            return ((Integer) this.variants.get(name)).intValue();
        }
    }

    public String getGenClassName(Class<?> clazz) {
        return "Fastjson_ASM_" + clazz.getSimpleName() + "_" + this.seed.incrementAndGet();
    }

    public String getGenFieldDeserializer(Class<?> clazz, FieldInfo fieldInfo) {
        return ("Fastjson_ASM__Field_" + clazz.getSimpleName()) + "_" + fieldInfo.getName() + "_" + this.seed.incrementAndGet();
    }

    public ASMDeserializerFactory() {
        this.seed = new AtomicLong();
        this.classLoader = new ASMClassLoader();
    }

    public ASMDeserializerFactory(ClassLoader parentClassLoader) {
        this.seed = new AtomicLong();
        this.classLoader = new ASMClassLoader(parentClassLoader);
    }

    public static final ASMDeserializerFactory getInstance() {
        return instance;
    }

    public boolean isExternalClass(Class<?> clazz) {
        return this.classLoader.isExternalClass(clazz);
    }

    public ObjectDeserializer createJavaBeanDeserializer(ParserConfig config, Class<?> clazz, Type type) throws Exception {
        Exception ex;
        Throwable th;
        if (clazz.isPrimitive()) {
            throw new IllegalArgumentException("not support type :" + clazz.getName());
        }
        String className = getGenClassName(clazz);
        ClassWriter cw = new ClassWriter();
        cw.visit(49, 33, className, "com/alibaba/fastjson/parser/deserializer/ASMJavaBeanDeserializer", null);
        DeserializeBeanInfo beanInfo = DeserializeBeanInfo.computeSetters(clazz, type);
        _init(cw, new Context(className, config, beanInfo, 3));
        _createInstance(cw, new Context(className, config, beanInfo, 3));
        _deserialze(cw, new Context(className, config, beanInfo, 4));
        _deserialzeArrayMapping(cw, new Context(className, config, beanInfo, 4));
        byte[] code = cw.toByteArray();
        if (JSON.DUMP_CLASS != null) {
            FileOutputStream fos = null;
            try {
                FileOutputStream fos2 = new FileOutputStream(JSON.DUMP_CLASS + File.separator + className + ".class");
                try {
                    fos2.write(code);
                    if (fos2 != null) {
                        fos2.close();
                    }
                } catch (Exception e) {
                    ex = e;
                    fos = fos2;
                    try {
                        System.err.println("FASTJSON dump class:" + className + "失败:" + ex.getMessage());
                        if (fos != null) {
                            fos.close();
                        }
                        return (ObjectDeserializer) this.classLoader.defineClassPublic(className, code, 0, code.length).getConstructor(new Class[]{ParserConfig.class, Class.class}).newInstance(new Object[]{config, clazz});
                    } catch (Throwable th2) {
                        th = th2;
                        if (fos != null) {
                            fos.close();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fos = fos2;
                    if (fos != null) {
                        fos.close();
                    }
                    throw th;
                }
            } catch (Exception e2) {
                ex = e2;
                System.err.println("FASTJSON dump class:" + className + "失败:" + ex.getMessage());
                if (fos != null) {
                    fos.close();
                }
                return (ObjectDeserializer) this.classLoader.defineClassPublic(className, code, 0, code.length).getConstructor(new Class[]{ParserConfig.class, Class.class}).newInstance(new Object[]{config, clazz});
            }
        }
        return (ObjectDeserializer) this.classLoader.defineClassPublic(className, code, 0, code.length).getConstructor(new Class[]{ParserConfig.class, Class.class}).newInstance(new Object[]{config, clazz});
    }

    void _setFlag(MethodVisitor mw, Context context, int i) {
        String varName = "_asm_flag_" + (i / 32);
        mw.visitVarInsn(21, context.var(varName));
        mw.visitLdcInsn(Integer.valueOf(1 << i));
        mw.visitInsn(128);
        mw.visitVarInsn(54, context.var(varName));
    }

    void _isFlag(MethodVisitor mw, Context context, int i, Label label) {
        mw.visitVarInsn(21, context.var("_asm_flag_" + (i / 32)));
        mw.visitLdcInsn(Integer.valueOf(1 << i));
        mw.visitInsn(126);
        mw.visitJumpInsn(153, label);
    }

    void _deserialzeArrayMapping(ClassWriter cw, Context context) {
        MethodVisitor mw = cw.visitMethod(1, "deserialzeArrayMapping", "(Lcom/alibaba/fastjson/parser/DefaultJSONParser;Ljava/lang/reflect/Type;Ljava/lang/Object;)Ljava/lang/Object;", null, null);
        defineVarLexer(context, mw);
        _createInstance(context, mw);
        List<FieldInfo> sortedFieldInfoList = context.getBeanInfo().getSortedFieldList();
        int fieldListSize = sortedFieldInfoList.size();
        int i = 0;
        while (i < fieldListSize) {
            boolean last = i == fieldListSize + -1;
            char seperator = last ? ']' : ',';
            FieldInfo fieldInfo = (FieldInfo) sortedFieldInfoList.get(i);
            Class fieldClass = fieldInfo.getFieldClass();
            Type fieldType = fieldInfo.getFieldType();
            if (fieldClass == Byte.TYPE || fieldClass == Short.TYPE || fieldClass == Integer.TYPE) {
                mw.visitVarInsn(25, context.var("lexer"));
                mw.visitVarInsn(16, seperator);
                mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/JSONLexerBase", "scanInt", "(C)I");
                mw.visitVarInsn(54, context.var(fieldInfo.getName() + "_asm"));
            } else if (fieldClass == Long.TYPE) {
                mw.visitVarInsn(25, context.var("lexer"));
                mw.visitVarInsn(16, seperator);
                mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/JSONLexerBase", "scanLong", "(C)J");
                mw.visitVarInsn(55, context.var(fieldInfo.getName() + "_asm", 2));
            } else if (fieldClass == Boolean.TYPE) {
                mw.visitVarInsn(25, context.var("lexer"));
                mw.visitVarInsn(16, seperator);
                mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/JSONLexerBase", "scanBoolean", "(C)Z");
                mw.visitVarInsn(54, context.var(fieldInfo.getName() + "_asm"));
            } else if (fieldClass == Float.TYPE) {
                mw.visitVarInsn(25, context.var("lexer"));
                mw.visitVarInsn(16, seperator);
                mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/JSONLexerBase", "scanFloat", "(C)F");
                mw.visitVarInsn(56, context.var(fieldInfo.getName() + "_asm"));
            } else if (fieldClass == Double.TYPE) {
                mw.visitVarInsn(25, context.var("lexer"));
                mw.visitVarInsn(16, seperator);
                mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/JSONLexerBase", "scanDouble", "(C)D");
                mw.visitVarInsn(57, context.var(fieldInfo.getName() + "_asm", 2));
            } else if (fieldClass == Character.TYPE) {
                mw.visitVarInsn(25, context.var("lexer"));
                mw.visitVarInsn(16, seperator);
                mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/JSONLexerBase", "scanString", "(C)Ljava/lang/String;");
                mw.visitInsn(3);
                mw.visitMethodInsn(182, "java/lang/String", "charAt", "(I)C");
                mw.visitVarInsn(54, context.var(fieldInfo.getName() + "_asm"));
            } else if (fieldClass == String.class) {
                mw.visitVarInsn(25, context.var("lexer"));
                mw.visitVarInsn(16, seperator);
                mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/JSONLexerBase", "scanString", "(C)Ljava/lang/String;");
                mw.visitVarInsn(58, context.var(fieldInfo.getName() + "_asm"));
            } else if (fieldClass.isEnum()) {
                mw.visitVarInsn(25, context.var("lexer"));
                mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(ASMUtils.getDesc(fieldClass)));
                mw.visitVarInsn(25, 1);
                mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/DefaultJSONParser", "getSymbolTable", "()Lcom/alibaba/fastjson/parser/SymbolTable;");
                mw.visitVarInsn(16, seperator);
                mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/JSONLexerBase", "scanEnum", "(Ljava/lang/Class;Lcom/alibaba/fastjson/parser/SymbolTable;C)Ljava/lang/Enum;");
                mw.visitTypeInsn(192, ASMUtils.getType(fieldClass));
                mw.visitVarInsn(58, context.var(fieldInfo.getName() + "_asm"));
            } else if (Collection.class.isAssignableFrom(fieldClass)) {
                Class itemClass = getCollectionItemClass(fieldType);
                if (itemClass == String.class) {
                    mw.visitVarInsn(25, context.var("lexer"));
                    mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(ASMUtils.getDesc(fieldClass)));
                    mw.visitVarInsn(16, seperator);
                    mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/JSONLexerBase", "scanStringArray", "(Ljava/lang/Class;C)Ljava/util/Collection;");
                    mw.visitVarInsn(58, context.var(fieldInfo.getName() + "_asm"));
                } else {
                    mw.visitVarInsn(25, 1);
                    if (i == 0) {
                        mw.visitFieldInsn(178, "com/alibaba/fastjson/parser/JSONToken", "LBRACKET", "I");
                    } else {
                        mw.visitFieldInsn(178, "com/alibaba/fastjson/parser/JSONToken", "COMMA", "I");
                    }
                    mw.visitFieldInsn(178, "com/alibaba/fastjson/parser/JSONToken", "LBRACKET", "I");
                    mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/DefaultJSONParser", "accept", "(II)V");
                    _newCollection(mw, fieldClass);
                    mw.visitInsn(89);
                    mw.visitVarInsn(58, context.var(fieldInfo.getName() + "_asm"));
                    _getCollectionFieldItemDeser(context, mw, fieldInfo, itemClass);
                    mw.visitVarInsn(25, 1);
                    mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(ASMUtils.getDesc(itemClass)));
                    mw.visitVarInsn(25, 3);
                    mw.visitMethodInsn(184, "com/alibaba/fastjson/util/ASMUtils", "parseArray", "(Ljava/util/Collection;Lcom/alibaba/fastjson/parser/deserializer/ObjectDeserializer;Lcom/alibaba/fastjson/parser/DefaultJSONParser;Ljava/lang/reflect/Type;Ljava/lang/Object;)V");
                }
            } else {
                mw.visitVarInsn(25, 1);
                if (i == 0) {
                    mw.visitFieldInsn(178, "com/alibaba/fastjson/parser/JSONToken", "LBRACKET", "I");
                } else {
                    mw.visitFieldInsn(178, "com/alibaba/fastjson/parser/JSONToken", "COMMA", "I");
                }
                mw.visitFieldInsn(178, "com/alibaba/fastjson/parser/JSONToken", "LBRACKET", "I");
                mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/DefaultJSONParser", "accept", "(II)V");
                _deserObject(context, mw, fieldInfo, fieldClass);
                mw.visitVarInsn(25, 1);
                if (last) {
                    mw.visitFieldInsn(178, "com/alibaba/fastjson/parser/JSONToken", "RBRACKET", "I");
                    mw.visitFieldInsn(178, "com/alibaba/fastjson/parser/JSONToken", "EOF", "I");
                } else {
                    mw.visitFieldInsn(178, "com/alibaba/fastjson/parser/JSONToken", "COMMA", "I");
                    mw.visitFieldInsn(178, "com/alibaba/fastjson/parser/JSONToken", "LBRACKET", "I");
                }
                mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/DefaultJSONParser", "accept", "(II)V");
            }
            i++;
        }
        _batchSet(context, mw, false);
        mw.visitVarInsn(25, context.var("lexer"));
        mw.visitFieldInsn(178, "com/alibaba/fastjson/parser/JSONToken", "COMMA", "I");
        mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/JSONLexerBase", "nextToken", "(I)V");
        mw.visitVarInsn(25, context.var("instance"));
        mw.visitInsn(176);
        mw.visitMaxs(5, context.getVariantCount());
        mw.visitEnd();
    }

    void _deserialze(ClassWriter cw, Context context) {
        if (context.getFieldInfoList().size() != 0) {
            Class<?> fieldClass;
            FieldInfo fieldInfo;
            Type fieldType;
            int i;
            for (FieldInfo fieldInfo2 : context.getFieldInfoList()) {
                fieldClass = fieldInfo2.getFieldClass();
                fieldType = fieldInfo2.getFieldType();
                if (fieldClass == Character.TYPE) {
                    return;
                }
                if (Collection.class.isAssignableFrom(fieldClass)) {
                    if (!(fieldType instanceof ParameterizedType) || !(((ParameterizedType) fieldType).getActualTypeArguments()[0] instanceof Class)) {
                        return;
                    }
                }
            }
            Collections.sort(context.getFieldInfoList());
            MethodVisitor mw = cw.visitMethod(1, "deserialze", "(Lcom/alibaba/fastjson/parser/DefaultJSONParser;Ljava/lang/reflect/Type;Ljava/lang/Object;)Ljava/lang/Object;", null, null);
            Label reset_ = new Label();
            Label super_ = new Label();
            Label return_ = new Label();
            Label end_ = new Label();
            defineVarLexer(context, mw);
            _isEnable(context, mw, Feature.SortFeidFastMatch);
            mw.visitJumpInsn(153, super_);
            Label next_ = new Label();
            mw.visitVarInsn(25, 0);
            mw.visitVarInsn(25, context.var("lexer"));
            mw.visitMethodInsn(183, "com/alibaba/fastjson/parser/deserializer/ASMJavaBeanDeserializer", "isSupportArrayToBean", "(Lcom/alibaba/fastjson/parser/JSONLexer;)Z");
            mw.visitJumpInsn(153, next_);
            mw.visitVarInsn(25, context.var("lexer"));
            mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/JSONLexerBase", "token", "()I");
            mw.visitFieldInsn(178, "com/alibaba/fastjson/parser/JSONToken", "LBRACKET", "I");
            mw.visitJumpInsn(160, next_);
            mw.visitVarInsn(25, 0);
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, 2);
            mw.visitVarInsn(25, 3);
            mw.visitMethodInsn(183, context.getClassName(), "deserialzeArrayMapping", "(Lcom/alibaba/fastjson/parser/DefaultJSONParser;Ljava/lang/reflect/Type;Ljava/lang/Object;)Ljava/lang/Object;");
            mw.visitInsn(176);
            mw.visitLabel(next_);
            mw.visitVarInsn(25, context.var("lexer"));
            mw.visitLdcInsn(context.getClazz().getName());
            mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/JSONLexerBase", "scanType", "(Ljava/lang/String;)I");
            mw.visitFieldInsn(178, "com/alibaba/fastjson/parser/JSONLexerBase", "NOT_MATCH", "I");
            mw.visitJumpInsn(159, super_);
            mw.visitVarInsn(25, 1);
            mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/DefaultJSONParser", "getContext", "()Lcom/alibaba/fastjson/parser/ParseContext;");
            mw.visitVarInsn(58, context.var("mark_context"));
            mw.visitInsn(3);
            mw.visitVarInsn(54, context.var("matchedCount"));
            _createInstance(context, mw);
            mw.visitVarInsn(25, 1);
            mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/DefaultJSONParser", "getContext", "()Lcom/alibaba/fastjson/parser/ParseContext;");
            mw.visitVarInsn(58, context.var("context"));
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, context.var("context"));
            mw.visitVarInsn(25, context.var("instance"));
            mw.visitVarInsn(25, 3);
            mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/DefaultJSONParser", "setContext", "(Lcom/alibaba/fastjson/parser/ParseContext;Ljava/lang/Object;Ljava/lang/Object;)Lcom/alibaba/fastjson/parser/ParseContext;");
            mw.visitVarInsn(58, context.var("childContext"));
            mw.visitVarInsn(25, context.var("lexer"));
            mw.visitFieldInsn(180, "com/alibaba/fastjson/parser/JSONLexerBase", "matchStat", "I");
            mw.visitFieldInsn(178, "com/alibaba/fastjson/parser/JSONLexerBase", "END", "I");
            mw.visitJumpInsn(159, return_);
            mw.visitInsn(3);
            mw.visitIntInsn(54, context.var("matchStat"));
            int fieldListSize = context.getFieldInfoList().size();
            for (i = 0; i < fieldListSize; i += 32) {
                mw.visitInsn(3);
                mw.visitVarInsn(54, context.var("_asm_flag_" + (i / 32)));
            }
            for (i = 0; i < fieldListSize; i++) {
                fieldInfo2 = (FieldInfo) context.getFieldInfoList().get(i);
                fieldClass = fieldInfo2.getFieldClass();
                if (fieldClass == Boolean.TYPE || fieldClass == Byte.TYPE || fieldClass == Short.TYPE || fieldClass == Integer.TYPE) {
                    mw.visitInsn(3);
                    mw.visitVarInsn(54, context.var(fieldInfo2.getName() + "_asm"));
                } else if (fieldClass == Long.TYPE) {
                    mw.visitInsn(9);
                    mw.visitVarInsn(55, context.var(fieldInfo2.getName() + "_asm", 2));
                } else if (fieldClass == Float.TYPE) {
                    mw.visitInsn(11);
                    mw.visitVarInsn(56, context.var(fieldInfo2.getName() + "_asm"));
                } else if (fieldClass == Double.TYPE) {
                    mw.visitInsn(14);
                    mw.visitVarInsn(57, context.var(fieldInfo2.getName() + "_asm", 2));
                } else {
                    if (fieldClass == String.class) {
                        Label flagEnd_ = new Label();
                        _isEnable(context, mw, Feature.InitStringFieldAsEmpty);
                        mw.visitJumpInsn(153, flagEnd_);
                        _setFlag(mw, context, i);
                        mw.visitLabel(flagEnd_);
                        mw.visitVarInsn(25, context.var("lexer"));
                        mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/JSONLexerBase", "stringDefaultValue", "()Ljava/lang/String;");
                    } else {
                        mw.visitInsn(1);
                    }
                    mw.visitTypeInsn(192, ASMUtils.getType(fieldClass));
                    mw.visitVarInsn(58, context.var(fieldInfo2.getName() + "_asm"));
                }
            }
            for (i = 0; i < fieldListSize; i++) {
                fieldInfo2 = (FieldInfo) context.getFieldInfoList().get(i);
                Class fieldClass2 = fieldInfo2.getFieldClass();
                fieldType = fieldInfo2.getFieldType();
                Label notMatch_ = new Label();
                if (fieldClass2 == Boolean.TYPE) {
                    mw.visitVarInsn(25, context.var("lexer"));
                    mw.visitVarInsn(25, 0);
                    mw.visitFieldInsn(180, context.getClassName(), fieldInfo2.getName() + "_asm_prefix__", "[C");
                    mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/JSONLexerBase", "scanFieldBoolean", "([C)Z");
                    mw.visitVarInsn(54, context.var(fieldInfo2.getName() + "_asm"));
                } else if (fieldClass2 == Byte.TYPE) {
                    mw.visitVarInsn(25, context.var("lexer"));
                    mw.visitVarInsn(25, 0);
                    mw.visitFieldInsn(180, context.getClassName(), fieldInfo2.getName() + "_asm_prefix__", "[C");
                    mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/JSONLexerBase", "scanFieldInt", "([C)I");
                    mw.visitVarInsn(54, context.var(fieldInfo2.getName() + "_asm"));
                } else if (fieldClass2 == Short.TYPE) {
                    mw.visitVarInsn(25, context.var("lexer"));
                    mw.visitVarInsn(25, 0);
                    mw.visitFieldInsn(180, context.getClassName(), fieldInfo2.getName() + "_asm_prefix__", "[C");
                    mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/JSONLexerBase", "scanFieldInt", "([C)I");
                    mw.visitVarInsn(54, context.var(fieldInfo2.getName() + "_asm"));
                } else if (fieldClass2 == Integer.TYPE) {
                    mw.visitVarInsn(25, context.var("lexer"));
                    mw.visitVarInsn(25, 0);
                    mw.visitFieldInsn(180, context.getClassName(), fieldInfo2.getName() + "_asm_prefix__", "[C");
                    mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/JSONLexerBase", "scanFieldInt", "([C)I");
                    mw.visitVarInsn(54, context.var(fieldInfo2.getName() + "_asm"));
                } else if (fieldClass2 == Long.TYPE) {
                    mw.visitVarInsn(25, context.var("lexer"));
                    mw.visitVarInsn(25, 0);
                    mw.visitFieldInsn(180, context.getClassName(), fieldInfo2.getName() + "_asm_prefix__", "[C");
                    mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/JSONLexerBase", "scanFieldLong", "([C)J");
                    mw.visitVarInsn(55, context.var(fieldInfo2.getName() + "_asm", 2));
                } else if (fieldClass2 == Float.TYPE) {
                    mw.visitVarInsn(25, context.var("lexer"));
                    mw.visitVarInsn(25, 0);
                    mw.visitFieldInsn(180, context.getClassName(), fieldInfo2.getName() + "_asm_prefix__", "[C");
                    mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/JSONLexerBase", "scanFieldFloat", "([C)F");
                    mw.visitVarInsn(56, context.var(fieldInfo2.getName() + "_asm"));
                } else if (fieldClass2 == Double.TYPE) {
                    mw.visitVarInsn(25, context.var("lexer"));
                    mw.visitVarInsn(25, 0);
                    mw.visitFieldInsn(180, context.getClassName(), fieldInfo2.getName() + "_asm_prefix__", "[C");
                    mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/JSONLexerBase", "scanFieldDouble", "([C)D");
                    mw.visitVarInsn(57, context.var(fieldInfo2.getName() + "_asm", 2));
                } else if (fieldClass2 == String.class) {
                    Label notEnd_ = new Label();
                    mw.visitIntInsn(21, context.var("matchStat"));
                    mw.visitInsn(7);
                    mw.visitJumpInsn(160, notEnd_);
                    mw.visitVarInsn(25, context.var("lexer"));
                    mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/JSONLexerBase", "stringDefaultValue", "()Ljava/lang/String;");
                    mw.visitVarInsn(58, context.var(fieldInfo2.getName() + "_asm"));
                    mw.visitJumpInsn(167, notMatch_);
                    mw.visitLabel(notEnd_);
                    mw.visitVarInsn(25, context.var("lexer"));
                    mw.visitVarInsn(25, 0);
                    mw.visitFieldInsn(180, context.getClassName(), fieldInfo2.getName() + "_asm_prefix__", "[C");
                    mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/JSONLexerBase", "scanFieldString", "([C)Ljava/lang/String;");
                    mw.visitVarInsn(58, context.var(fieldInfo2.getName() + "_asm"));
                } else if (fieldClass2.isEnum()) {
                    mw.visitVarInsn(25, context.var("lexer"));
                    mw.visitVarInsn(25, 0);
                    mw.visitFieldInsn(180, context.getClassName(), fieldInfo2.getName() + "_asm_prefix__", "[C");
                    Label enumNull_ = new Label();
                    mw.visitInsn(1);
                    mw.visitTypeInsn(192, ASMUtils.getType(fieldClass2));
                    mw.visitVarInsn(58, context.var(fieldInfo2.getName() + "_asm"));
                    mw.visitVarInsn(25, 1);
                    mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/DefaultJSONParser", "getSymbolTable", "()Lcom/alibaba/fastjson/parser/SymbolTable;");
                    mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/JSONLexerBase", "scanFieldSymbol", "([CLcom/alibaba/fastjson/parser/SymbolTable;)Ljava/lang/String;");
                    mw.visitInsn(89);
                    mw.visitVarInsn(58, context.var(fieldInfo2.getName() + "_asm_enumName"));
                    mw.visitJumpInsn(198, enumNull_);
                    mw.visitVarInsn(25, context.var(fieldInfo2.getName() + "_asm_enumName"));
                    mw.visitMethodInsn(184, ASMUtils.getType(fieldClass2), "valueOf", "(Ljava/lang/String;)" + ASMUtils.getDesc(fieldClass2));
                    mw.visitVarInsn(58, context.var(fieldInfo2.getName() + "_asm"));
                    mw.visitLabel(enumNull_);
                } else {
                    if (Collection.class.isAssignableFrom(fieldClass2)) {
                        mw.visitVarInsn(25, context.var("lexer"));
                        mw.visitVarInsn(25, 0);
                        mw.visitFieldInsn(180, context.getClassName(), fieldInfo2.getName() + "_asm_prefix__", "[C");
                        Class<?> itemClass = getCollectionItemClass(fieldType);
                        if (itemClass == String.class) {
                            mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(ASMUtils.getDesc(fieldClass2)));
                            mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/JSONLexerBase", "scanFieldStringArray", "([CLjava/lang/Class;)" + ASMUtils.getDesc(Collection.class));
                            mw.visitVarInsn(58, context.var(fieldInfo2.getName() + "_asm"));
                        } else {
                            _deserialze_list_obj(context, mw, reset_, fieldInfo2, fieldClass2, itemClass, i);
                            if (i == fieldListSize - 1) {
                                _deserialize_endCheck(context, mw, reset_);
                            }
                        }
                    } else {
                        _deserialze_obj(context, mw, reset_, fieldInfo2, fieldClass2, i);
                        if (i == fieldListSize - 1) {
                            _deserialize_endCheck(context, mw, reset_);
                        }
                    }
                }
                mw.visitVarInsn(25, context.var("lexer"));
                mw.visitFieldInsn(180, "com/alibaba/fastjson/parser/JSONLexerBase", "matchStat", "I");
                Label flag_ = new Label();
                mw.visitJumpInsn(158, flag_);
                _setFlag(mw, context, i);
                mw.visitLabel(flag_);
                mw.visitVarInsn(25, context.var("lexer"));
                mw.visitFieldInsn(180, "com/alibaba/fastjson/parser/JSONLexerBase", "matchStat", "I");
                mw.visitInsn(89);
                mw.visitVarInsn(54, context.var("matchStat"));
                mw.visitFieldInsn(178, "com/alibaba/fastjson/parser/JSONLexerBase", "NOT_MATCH", "I");
                mw.visitJumpInsn(159, reset_);
                mw.visitVarInsn(25, context.var("lexer"));
                mw.visitFieldInsn(180, "com/alibaba/fastjson/parser/JSONLexerBase", "matchStat", "I");
                mw.visitJumpInsn(158, notMatch_);
                mw.visitVarInsn(21, context.var("matchedCount"));
                mw.visitInsn(4);
                mw.visitInsn(96);
                mw.visitVarInsn(54, context.var("matchedCount"));
                mw.visitVarInsn(25, context.var("lexer"));
                mw.visitFieldInsn(180, "com/alibaba/fastjson/parser/JSONLexerBase", "matchStat", "I");
                mw.visitFieldInsn(178, "com/alibaba/fastjson/parser/JSONLexerBase", "END", "I");
                mw.visitJumpInsn(159, end_);
                mw.visitLabel(notMatch_);
                if (i == fieldListSize - 1) {
                    mw.visitVarInsn(25, context.var("lexer"));
                    mw.visitFieldInsn(180, "com/alibaba/fastjson/parser/JSONLexerBase", "matchStat", "I");
                    mw.visitFieldInsn(178, "com/alibaba/fastjson/parser/JSONLexerBase", "END", "I");
                    mw.visitJumpInsn(160, reset_);
                }
            }
            mw.visitLabel(end_);
            if (!(context.getClazz().isInterface() || Modifier.isAbstract(context.getClazz().getModifiers()))) {
                _batchSet(context, mw);
            }
            mw.visitLabel(return_);
            _setContext(context, mw);
            mw.visitVarInsn(25, context.var("instance"));
            mw.visitInsn(176);
            mw.visitLabel(reset_);
            _batchSet(context, mw);
            mw.visitVarInsn(25, 0);
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, 2);
            mw.visitVarInsn(25, 3);
            mw.visitVarInsn(25, context.var("instance"));
            mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/deserializer/ASMJavaBeanDeserializer", "parseRest", "(Lcom/alibaba/fastjson/parser/DefaultJSONParser;Ljava/lang/reflect/Type;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
            mw.visitTypeInsn(192, ASMUtils.getType(context.getClazz()));
            mw.visitInsn(176);
            mw.visitLabel(super_);
            mw.visitVarInsn(25, 0);
            mw.visitVarInsn(25, 1);
            mw.visitVarInsn(25, 2);
            mw.visitVarInsn(25, 3);
            mw.visitMethodInsn(183, "com/alibaba/fastjson/parser/deserializer/ASMJavaBeanDeserializer", "deserialze", "(Lcom/alibaba/fastjson/parser/DefaultJSONParser;Ljava/lang/reflect/Type;Ljava/lang/Object;)Ljava/lang/Object;");
            mw.visitInsn(176);
            mw.visitMaxs(5, context.getVariantCount());
            mw.visitEnd();
        }
    }

    private Class<?> getCollectionItemClass(Type fieldType) {
        if (!(fieldType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type actualTypeArgument = ((ParameterizedType) fieldType).getActualTypeArguments()[0];
        if (actualTypeArgument instanceof Class) {
            Class<?> cls = (Class) actualTypeArgument;
            if (Modifier.isPublic(cls.getModifiers())) {
                return cls;
            }
            throw new ASMException("can not create ASMParser");
        }
        throw new ASMException("can not create ASMParser");
    }

    private void _isEnable(Context context, MethodVisitor mw, Feature feature) {
        mw.visitVarInsn(25, context.var("lexer"));
        mw.visitFieldInsn(178, "com/alibaba/fastjson/parser/Feature", feature.name(), "Lcom/alibaba/fastjson/parser/Feature;");
        mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/JSONLexerBase", "isEnabled", "(Lcom/alibaba/fastjson/parser/Feature;)Z");
    }

    private void defineVarLexer(Context context, MethodVisitor mw) {
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/DefaultJSONParser", "getLexer", "()Lcom/alibaba/fastjson/parser/JSONLexer;");
        mw.visitTypeInsn(192, "com/alibaba/fastjson/parser/JSONLexerBase");
        mw.visitVarInsn(58, context.var("lexer"));
    }

    private void _createInstance(Context context, MethodVisitor mw) {
        if (Modifier.isPublic(context.getBeanInfo().getDefaultConstructor().getModifiers())) {
            mw.visitTypeInsn(187, ASMUtils.getType(context.getClazz()));
            mw.visitInsn(89);
            mw.visitMethodInsn(183, ASMUtils.getType(context.getClazz()), "<init>", "()V");
            mw.visitVarInsn(58, context.var("instance"));
            return;
        }
        mw.visitVarInsn(25, 0);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(183, "com/alibaba/fastjson/parser/deserializer/ASMJavaBeanDeserializer", "createInstance", "(Lcom/alibaba/fastjson/parser/DefaultJSONParser;)Ljava/lang/Object;");
        mw.visitTypeInsn(192, ASMUtils.getType(context.getClazz()));
        mw.visitVarInsn(58, context.var("instance"));
    }

    private void _batchSet(Context context, MethodVisitor mw) {
        _batchSet(context, mw, true);
    }

    private void _batchSet(Context context, MethodVisitor mw, boolean flag) {
        int size = context.getFieldInfoList().size();
        for (int i = 0; i < size; i++) {
            Label notSet_ = new Label();
            if (flag) {
                _isFlag(mw, context, i, notSet_);
            }
            _loadAndSet(context, mw, (FieldInfo) context.getFieldInfoList().get(i));
            if (flag) {
                mw.visitLabel(notSet_);
            }
        }
    }

    private void _loadAndSet(Context context, MethodVisitor mw, FieldInfo fieldInfo) {
        Class<?> fieldClass = fieldInfo.getFieldClass();
        Type fieldType = fieldInfo.getFieldType();
        if (fieldClass == Boolean.TYPE) {
            mw.visitVarInsn(25, context.var("instance"));
            mw.visitVarInsn(21, context.var(fieldInfo.getName() + "_asm"));
            _set(context, mw, fieldInfo);
        } else if (fieldClass == Byte.TYPE || fieldClass == Short.TYPE || fieldClass == Integer.TYPE || fieldClass == Character.TYPE) {
            mw.visitVarInsn(25, context.var("instance"));
            mw.visitVarInsn(21, context.var(fieldInfo.getName() + "_asm"));
            _set(context, mw, fieldInfo);
        } else if (fieldClass == Long.TYPE) {
            mw.visitVarInsn(25, context.var("instance"));
            mw.visitVarInsn(22, context.var(fieldInfo.getName() + "_asm", 2));
            if (fieldInfo.getMethod() != null) {
                mw.visitMethodInsn(182, ASMUtils.getType(context.getClazz()), fieldInfo.getMethod().getName(), ASMUtils.getDesc(fieldInfo.getMethod()));
                if (!fieldInfo.getMethod().getReturnType().equals(Void.TYPE)) {
                    mw.visitInsn(87);
                    return;
                }
                return;
            }
            mw.visitFieldInsn(181, ASMUtils.getType(fieldInfo.getDeclaringClass()), fieldInfo.getField().getName(), ASMUtils.getDesc(fieldInfo.getFieldClass()));
        } else if (fieldClass == Float.TYPE) {
            mw.visitVarInsn(25, context.var("instance"));
            mw.visitVarInsn(23, context.var(fieldInfo.getName() + "_asm"));
            _set(context, mw, fieldInfo);
        } else if (fieldClass == Double.TYPE) {
            mw.visitVarInsn(25, context.var("instance"));
            mw.visitVarInsn(24, context.var(fieldInfo.getName() + "_asm", 2));
            _set(context, mw, fieldInfo);
        } else if (fieldClass == String.class) {
            mw.visitVarInsn(25, context.var("instance"));
            mw.visitVarInsn(25, context.var(fieldInfo.getName() + "_asm"));
            _set(context, mw, fieldInfo);
        } else if (fieldClass.isEnum()) {
            mw.visitVarInsn(25, context.var("instance"));
            mw.visitVarInsn(25, context.var(fieldInfo.getName() + "_asm"));
            _set(context, mw, fieldInfo);
        } else if (Collection.class.isAssignableFrom(fieldClass)) {
            mw.visitVarInsn(25, context.var("instance"));
            if (getCollectionItemClass(fieldType) == String.class) {
                mw.visitVarInsn(25, context.var(fieldInfo.getName() + "_asm"));
                mw.visitTypeInsn(192, ASMUtils.getType(fieldClass));
            } else {
                mw.visitVarInsn(25, context.var(fieldInfo.getName() + "_asm"));
            }
            _set(context, mw, fieldInfo);
        } else {
            mw.visitVarInsn(25, context.var("instance"));
            mw.visitVarInsn(25, context.var(fieldInfo.getName() + "_asm"));
            _set(context, mw, fieldInfo);
        }
    }

    private void _set(Context context, MethodVisitor mw, FieldInfo fieldInfo) {
        if (fieldInfo.getMethod() != null) {
            mw.visitMethodInsn(182, ASMUtils.getType(fieldInfo.getDeclaringClass()), fieldInfo.getMethod().getName(), ASMUtils.getDesc(fieldInfo.getMethod()));
            if (!fieldInfo.getMethod().getReturnType().equals(Void.TYPE)) {
                mw.visitInsn(87);
                return;
            }
            return;
        }
        mw.visitFieldInsn(181, ASMUtils.getType(fieldInfo.getDeclaringClass()), fieldInfo.getField().getName(), ASMUtils.getDesc(fieldInfo.getFieldClass()));
    }

    private void _setContext(Context context, MethodVisitor mw) {
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, context.var("context"));
        mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/DefaultJSONParser", "setContext", "(Lcom/alibaba/fastjson/parser/ParseContext;)V");
        Label endIf_ = new Label();
        mw.visitVarInsn(25, context.var("childContext"));
        mw.visitJumpInsn(198, endIf_);
        mw.visitVarInsn(25, context.var("childContext"));
        mw.visitVarInsn(25, context.var("instance"));
        mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/ParseContext", "setObject", "(Ljava/lang/Object;)V");
        mw.visitLabel(endIf_);
    }

    private void _deserialize_endCheck(Context context, MethodVisitor mw, Label reset_) {
        Label _end_if = new Label();
        mw.visitIntInsn(21, context.var("matchedCount"));
        mw.visitJumpInsn(158, reset_);
        mw.visitVarInsn(25, context.var("lexer"));
        mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/JSONLexerBase", "token", "()I");
        mw.visitFieldInsn(178, "com/alibaba/fastjson/parser/JSONToken", "RBRACE", "I");
        mw.visitJumpInsn(160, reset_);
        mw.visitVarInsn(25, context.var("lexer"));
        mw.visitFieldInsn(178, "com/alibaba/fastjson/parser/JSONToken", "COMMA", "I");
        mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/JSONLexerBase", "nextToken", "(I)V");
        mw.visitLabel(_end_if);
    }

    private void _deserialze_list_obj(Context context, MethodVisitor mw, Label reset_, FieldInfo fieldInfo, Class<?> fieldClass, Class<?> itemType, int i) {
        Label matched_ = new Label();
        Label _end_if = new Label();
        mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/JSONLexerBase", "matchField", "([C)Z");
        mw.visitJumpInsn(154, matched_);
        mw.visitInsn(1);
        mw.visitVarInsn(58, context.var(fieldInfo.getName() + "_asm"));
        mw.visitJumpInsn(167, _end_if);
        mw.visitLabel(matched_);
        _setFlag(mw, context, i);
        Label valueNotNull_ = new Label();
        mw.visitVarInsn(25, context.var("lexer"));
        mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/JSONLexerBase", "token", "()I");
        mw.visitFieldInsn(178, "com/alibaba/fastjson/parser/JSONToken", "NULL", "I");
        mw.visitJumpInsn(160, valueNotNull_);
        mw.visitVarInsn(25, context.var("lexer"));
        mw.visitFieldInsn(178, "com/alibaba/fastjson/parser/JSONToken", "COMMA", "I");
        mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/JSONLexerBase", "nextToken", "(I)V");
        mw.visitInsn(1);
        mw.visitTypeInsn(192, ASMUtils.getType(fieldClass));
        mw.visitVarInsn(58, context.var(fieldInfo.getName() + "_asm"));
        mw.visitLabel(valueNotNull_);
        mw.visitVarInsn(25, context.var("lexer"));
        mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/JSONLexerBase", "token", "()I");
        mw.visitFieldInsn(178, "com/alibaba/fastjson/parser/JSONToken", "LBRACKET", "I");
        mw.visitJumpInsn(160, reset_);
        _getCollectionFieldItemDeser(context, mw, fieldInfo, itemType);
        mw.visitMethodInsn(185, "com/alibaba/fastjson/parser/deserializer/ObjectDeserializer", "getFastMatchToken", "()I");
        mw.visitVarInsn(54, context.var("fastMatchToken"));
        mw.visitVarInsn(25, context.var("lexer"));
        mw.visitVarInsn(21, context.var("fastMatchToken"));
        mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/JSONLexerBase", "nextToken", "(I)V");
        _newCollection(mw, fieldClass);
        mw.visitVarInsn(58, context.var(fieldInfo.getName() + "_asm"));
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/DefaultJSONParser", "getContext", "()Lcom/alibaba/fastjson/parser/ParseContext;");
        mw.visitVarInsn(58, context.var("listContext"));
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, context.var(fieldInfo.getName() + "_asm"));
        mw.visitLdcInsn(fieldInfo.getName());
        mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/DefaultJSONParser", "setContext", "(Ljava/lang/Object;Ljava/lang/Object;)Lcom/alibaba/fastjson/parser/ParseContext;");
        mw.visitInsn(87);
        Label loop_ = new Label();
        Label loop_end_ = new Label();
        mw.visitInsn(3);
        mw.visitVarInsn(54, context.var("i"));
        mw.visitLabel(loop_);
        mw.visitVarInsn(25, context.var("lexer"));
        mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/JSONLexerBase", "token", "()I");
        mw.visitFieldInsn(178, "com/alibaba/fastjson/parser/JSONToken", "RBRACKET", "I");
        mw.visitJumpInsn(159, loop_end_);
        mw.visitVarInsn(25, 0);
        mw.visitFieldInsn(180, context.getClassName(), fieldInfo.getName() + "_asm_list_item_deser__", "Lcom/alibaba/fastjson/parser/deserializer/ObjectDeserializer;");
        mw.visitVarInsn(25, 1);
        mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(ASMUtils.getDesc((Class) itemType)));
        mw.visitVarInsn(21, context.var("i"));
        mw.visitMethodInsn(184, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
        mw.visitMethodInsn(185, "com/alibaba/fastjson/parser/deserializer/ObjectDeserializer", "deserialze", "(Lcom/alibaba/fastjson/parser/DefaultJSONParser;Ljava/lang/reflect/Type;Ljava/lang/Object;)Ljava/lang/Object;");
        mw.visitVarInsn(58, context.var("list_item_value"));
        mw.visitIincInsn(context.var("i"), 1);
        mw.visitVarInsn(25, context.var(fieldInfo.getName() + "_asm"));
        mw.visitVarInsn(25, context.var("list_item_value"));
        if (fieldClass.isInterface()) {
            mw.visitMethodInsn(185, ASMUtils.getType(fieldClass), PostPicturePreviewAdapter.KEY_ADD, "(Ljava/lang/Object;)Z");
        } else {
            mw.visitMethodInsn(182, ASMUtils.getType(fieldClass), PostPicturePreviewAdapter.KEY_ADD, "(Ljava/lang/Object;)Z");
        }
        mw.visitInsn(87);
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, context.var(fieldInfo.getName() + "_asm"));
        mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/DefaultJSONParser", "checkListResolve", "(Ljava/util/Collection;)V");
        mw.visitVarInsn(25, context.var("lexer"));
        mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/JSONLexerBase", "token", "()I");
        mw.visitFieldInsn(178, "com/alibaba/fastjson/parser/JSONToken", "COMMA", "I");
        mw.visitJumpInsn(160, loop_);
        mw.visitVarInsn(25, context.var("lexer"));
        mw.visitVarInsn(21, context.var("fastMatchToken"));
        mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/JSONLexerBase", "nextToken", "(I)V");
        mw.visitJumpInsn(167, loop_);
        mw.visitLabel(loop_end_);
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, context.var("listContext"));
        mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/DefaultJSONParser", "setContext", "(Lcom/alibaba/fastjson/parser/ParseContext;)V");
        mw.visitVarInsn(25, context.var("lexer"));
        mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/JSONLexerBase", "token", "()I");
        mw.visitFieldInsn(178, "com/alibaba/fastjson/parser/JSONToken", "RBRACKET", "I");
        mw.visitJumpInsn(160, reset_);
        mw.visitVarInsn(25, context.var("lexer"));
        mw.visitFieldInsn(178, "com/alibaba/fastjson/parser/JSONToken", "COMMA", "I");
        mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/JSONLexerBase", "nextToken", "(I)V");
        mw.visitLabel(_end_if);
    }

    private void _getCollectionFieldItemDeser(Context context, MethodVisitor mw, FieldInfo fieldInfo, Class<?> itemType) {
        Label notNull_ = new Label();
        mw.visitVarInsn(25, 0);
        mw.visitFieldInsn(180, context.getClassName(), fieldInfo.getName() + "_asm_list_item_deser__", "Lcom/alibaba/fastjson/parser/deserializer/ObjectDeserializer;");
        mw.visitJumpInsn(199, notNull_);
        mw.visitVarInsn(25, 0);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/DefaultJSONParser", "getConfig", "()Lcom/alibaba/fastjson/parser/ParserConfig;");
        mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(ASMUtils.getDesc((Class) itemType)));
        mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/ParserConfig", "getDeserializer", "(Ljava/lang/reflect/Type;)Lcom/alibaba/fastjson/parser/deserializer/ObjectDeserializer;");
        mw.visitFieldInsn(181, context.getClassName(), fieldInfo.getName() + "_asm_list_item_deser__", "Lcom/alibaba/fastjson/parser/deserializer/ObjectDeserializer;");
        mw.visitLabel(notNull_);
        mw.visitVarInsn(25, 0);
        mw.visitFieldInsn(180, context.getClassName(), fieldInfo.getName() + "_asm_list_item_deser__", "Lcom/alibaba/fastjson/parser/deserializer/ObjectDeserializer;");
    }

    private void _newCollection(MethodVisitor mw, Class<?> fieldClass) {
        if (fieldClass.isAssignableFrom(ArrayList.class)) {
            mw.visitTypeInsn(187, "java/util/ArrayList");
            mw.visitInsn(89);
            mw.visitMethodInsn(183, "java/util/ArrayList", "<init>", "()V");
        } else if (fieldClass.isAssignableFrom(LinkedList.class)) {
            mw.visitTypeInsn(187, ASMUtils.getType(LinkedList.class));
            mw.visitInsn(89);
            mw.visitMethodInsn(183, ASMUtils.getType(LinkedList.class), "<init>", "()V");
        } else if (fieldClass.isAssignableFrom(HashSet.class)) {
            mw.visitTypeInsn(187, ASMUtils.getType(HashSet.class));
            mw.visitInsn(89);
            mw.visitMethodInsn(183, ASMUtils.getType(HashSet.class), "<init>", "()V");
        } else if (fieldClass.isAssignableFrom(TreeSet.class)) {
            mw.visitTypeInsn(187, ASMUtils.getType(TreeSet.class));
            mw.visitInsn(89);
            mw.visitMethodInsn(183, ASMUtils.getType(TreeSet.class), "<init>", "()V");
        } else {
            mw.visitTypeInsn(187, ASMUtils.getType(fieldClass));
            mw.visitInsn(89);
            mw.visitMethodInsn(183, ASMUtils.getType(fieldClass), "<init>", "()V");
        }
        mw.visitTypeInsn(192, ASMUtils.getType(fieldClass));
    }

    private void _deserialze_obj(Context context, MethodVisitor mw, Label reset_, FieldInfo fieldInfo, Class<?> fieldClass, int i) {
        Label matched_ = new Label();
        Label _end_if = new Label();
        mw.visitVarInsn(25, context.var("lexer"));
        mw.visitVarInsn(25, 0);
        mw.visitFieldInsn(180, context.getClassName(), fieldInfo.getName() + "_asm_prefix__", "[C");
        mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/JSONLexerBase", "matchField", "([C)Z");
        mw.visitJumpInsn(154, matched_);
        mw.visitInsn(1);
        mw.visitVarInsn(58, context.var(fieldInfo.getName() + "_asm"));
        mw.visitJumpInsn(167, _end_if);
        mw.visitLabel(matched_);
        _setFlag(mw, context, i);
        mw.visitVarInsn(21, context.var("matchedCount"));
        mw.visitInsn(4);
        mw.visitInsn(96);
        mw.visitVarInsn(54, context.var("matchedCount"));
        _deserObject(context, mw, fieldInfo, fieldClass);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/DefaultJSONParser", "getResolveStatus", "()I");
        mw.visitFieldInsn(178, "com/alibaba/fastjson/parser/DefaultJSONParser", "NeedToResolve", "I");
        mw.visitJumpInsn(160, _end_if);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/DefaultJSONParser", "getLastResolveTask", "()Lcom/alibaba/fastjson/parser/DefaultJSONParser$ResolveTask;");
        mw.visitVarInsn(58, context.var("resolveTask"));
        mw.visitVarInsn(25, context.var("resolveTask"));
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/DefaultJSONParser", "getContext", "()Lcom/alibaba/fastjson/parser/ParseContext;");
        mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/DefaultJSONParser$ResolveTask", "setOwnerContext", "(Lcom/alibaba/fastjson/parser/ParseContext;)V");
        mw.visitVarInsn(25, context.var("resolveTask"));
        mw.visitVarInsn(25, 0);
        mw.visitLdcInsn(fieldInfo.getName());
        mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/deserializer/ASMJavaBeanDeserializer", "getFieldDeserializer", "(Ljava/lang/String;)Lcom/alibaba/fastjson/parser/deserializer/FieldDeserializer;");
        mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/DefaultJSONParser$ResolveTask", "setFieldDeserializer", "(Lcom/alibaba/fastjson/parser/deserializer/FieldDeserializer;)V");
        mw.visitVarInsn(25, 1);
        mw.visitFieldInsn(178, "com/alibaba/fastjson/parser/DefaultJSONParser", "NONE", "I");
        mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/DefaultJSONParser", "setResolveStatus", "(I)V");
        mw.visitLabel(_end_if);
    }

    private void _deserObject(Context context, MethodVisitor mw, FieldInfo fieldInfo, Class<?> fieldClass) {
        _getFieldDeser(context, mw, fieldInfo);
        mw.visitVarInsn(25, 1);
        if (fieldInfo.getFieldType() instanceof Class) {
            mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(ASMUtils.getDesc(fieldInfo.getFieldClass())));
        } else {
            mw.visitVarInsn(25, 0);
            mw.visitLdcInsn(fieldInfo.getName());
            mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/deserializer/ASMJavaBeanDeserializer", "getFieldType", "(Ljava/lang/String;)Ljava/lang/reflect/Type;");
        }
        mw.visitLdcInsn(fieldInfo.getName());
        mw.visitMethodInsn(185, "com/alibaba/fastjson/parser/deserializer/ObjectDeserializer", "deserialze", "(Lcom/alibaba/fastjson/parser/DefaultJSONParser;Ljava/lang/reflect/Type;Ljava/lang/Object;)Ljava/lang/Object;");
        mw.visitTypeInsn(192, ASMUtils.getType(fieldClass));
        mw.visitVarInsn(58, context.var(fieldInfo.getName() + "_asm"));
    }

    private void _getFieldDeser(Context context, MethodVisitor mw, FieldInfo fieldInfo) {
        Label notNull_ = new Label();
        mw.visitVarInsn(25, 0);
        mw.visitFieldInsn(180, context.getClassName(), fieldInfo.getName() + "_asm_deser__", "Lcom/alibaba/fastjson/parser/deserializer/ObjectDeserializer;");
        mw.visitJumpInsn(199, notNull_);
        mw.visitVarInsn(25, 0);
        mw.visitVarInsn(25, 1);
        mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/DefaultJSONParser", "getConfig", "()Lcom/alibaba/fastjson/parser/ParserConfig;");
        mw.visitLdcInsn(com.alibaba.fastjson.asm.Type.getType(ASMUtils.getDesc(fieldInfo.getFieldClass())));
        mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/ParserConfig", "getDeserializer", "(Ljava/lang/reflect/Type;)Lcom/alibaba/fastjson/parser/deserializer/ObjectDeserializer;");
        mw.visitFieldInsn(181, context.getClassName(), fieldInfo.getName() + "_asm_deser__", "Lcom/alibaba/fastjson/parser/deserializer/ObjectDeserializer;");
        mw.visitLabel(notNull_);
        mw.visitVarInsn(25, 0);
        mw.visitFieldInsn(180, context.getClassName(), fieldInfo.getName() + "_asm_deser__", "Lcom/alibaba/fastjson/parser/deserializer/ObjectDeserializer;");
    }

    public FieldDeserializer createFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo) throws Exception {
        Class<?> fieldClass = fieldInfo.getFieldClass();
        if (fieldClass == Integer.TYPE || fieldClass == Long.TYPE || fieldClass == String.class) {
            return createStringFieldDeserializer(mapping, clazz, fieldInfo);
        }
        return mapping.createFieldDeserializerWithoutASM(mapping, clazz, fieldInfo);
    }

    public FieldDeserializer createStringFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo) throws Exception {
        Class<?> superClass;
        int INVAKE_TYPE;
        Class<?> fieldClass = fieldInfo.getFieldClass();
        Method method = fieldInfo.getMethod();
        String className = getGenFieldDeserializer(clazz, fieldInfo);
        ClassWriter cw = new ClassWriter();
        if (fieldClass == Integer.TYPE) {
            superClass = IntegerFieldDeserializer.class;
        } else if (fieldClass == Long.TYPE) {
            superClass = LongFieldDeserializer.class;
        } else {
            superClass = StringFieldDeserializer.class;
        }
        if (clazz.isInterface()) {
            INVAKE_TYPE = 185;
        } else {
            INVAKE_TYPE = 182;
        }
        cw.visit(49, 33, className, ASMUtils.getType(superClass), null);
        MethodVisitor mw = cw.visitMethod(1, "<init>", "(Lcom/alibaba/fastjson/parser/ParserConfig;Ljava/lang/Class;Lcom/alibaba/fastjson/util/FieldInfo;)V", null, null);
        mw.visitVarInsn(25, 0);
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, 2);
        mw.visitVarInsn(25, 3);
        mw.visitMethodInsn(183, ASMUtils.getType(superClass), "<init>", "(Lcom/alibaba/fastjson/parser/ParserConfig;Ljava/lang/Class;Lcom/alibaba/fastjson/util/FieldInfo;)V");
        mw.visitInsn(177);
        mw.visitMaxs(4, 6);
        mw.visitEnd();
        if (method != null) {
            if (fieldClass == Integer.TYPE) {
                mw = cw.visitMethod(1, "setValue", "(Ljava/lang/Object;I)V", null, null);
                mw.visitVarInsn(25, 1);
                mw.visitTypeInsn(192, ASMUtils.getType(method.getDeclaringClass()));
                mw.visitVarInsn(21, 2);
                mw.visitMethodInsn(INVAKE_TYPE, ASMUtils.getType(method.getDeclaringClass()), method.getName(), ASMUtils.getDesc(method));
                mw.visitInsn(177);
                mw.visitMaxs(3, 3);
                mw.visitEnd();
            } else if (fieldClass == Long.TYPE) {
                mw = cw.visitMethod(1, "setValue", "(Ljava/lang/Object;J)V", null, null);
                mw.visitVarInsn(25, 1);
                mw.visitTypeInsn(192, ASMUtils.getType(method.getDeclaringClass()));
                mw.visitVarInsn(22, 2);
                mw.visitMethodInsn(INVAKE_TYPE, ASMUtils.getType(method.getDeclaringClass()), method.getName(), ASMUtils.getDesc(method));
                mw.visitInsn(177);
                mw.visitMaxs(3, 4);
                mw.visitEnd();
            } else {
                mw = cw.visitMethod(1, "setValue", "(Ljava/lang/Object;Ljava/lang/Object;)V", null, null);
                mw.visitVarInsn(25, 1);
                mw.visitTypeInsn(192, ASMUtils.getType(method.getDeclaringClass()));
                mw.visitVarInsn(25, 2);
                mw.visitTypeInsn(192, ASMUtils.getType(fieldClass));
                mw.visitMethodInsn(INVAKE_TYPE, ASMUtils.getType(method.getDeclaringClass()), method.getName(), ASMUtils.getDesc(method));
                mw.visitInsn(177);
                mw.visitMaxs(3, 3);
                mw.visitEnd();
            }
        }
        byte[] code = cw.toByteArray();
        return (FieldDeserializer) this.classLoader.defineClassPublic(className, code, 0, code.length).getConstructor(new Class[]{ParserConfig.class, Class.class, FieldInfo.class}).newInstance(new Object[]{mapping, clazz, fieldInfo});
    }

    private void _init(ClassWriter cw, Context context) {
        int i;
        int size = context.getFieldInfoList().size();
        for (i = 0; i < size; i++) {
            cw.visitField(1, ((FieldInfo) context.getFieldInfoList().get(i)).getName() + "_asm_prefix__", "[C").visitEnd();
        }
        size = context.getFieldInfoList().size();
        for (i = 0; i < size; i++) {
            FieldInfo fieldInfo = (FieldInfo) context.getFieldInfoList().get(i);
            Class<?> fieldClass = fieldInfo.getFieldClass();
            if (!(fieldClass.isPrimitive() || fieldClass.isEnum())) {
                if (Collection.class.isAssignableFrom(fieldClass)) {
                    cw.visitField(1, fieldInfo.getName() + "_asm_list_item_deser__", "Lcom/alibaba/fastjson/parser/deserializer/ObjectDeserializer;").visitEnd();
                } else {
                    cw.visitField(1, fieldInfo.getName() + "_asm_deser__", "Lcom/alibaba/fastjson/parser/deserializer/ObjectDeserializer;").visitEnd();
                }
            }
        }
        MethodVisitor mw = cw.visitMethod(1, "<init>", "(Lcom/alibaba/fastjson/parser/ParserConfig;Ljava/lang/Class;)V", null, null);
        mw.visitVarInsn(25, 0);
        mw.visitVarInsn(25, 1);
        mw.visitVarInsn(25, 2);
        mw.visitMethodInsn(183, "com/alibaba/fastjson/parser/deserializer/ASMJavaBeanDeserializer", "<init>", "(Lcom/alibaba/fastjson/parser/ParserConfig;Ljava/lang/Class;)V");
        mw.visitVarInsn(25, 0);
        mw.visitFieldInsn(180, "com/alibaba/fastjson/parser/deserializer/ASMJavaBeanDeserializer", "serializer", "Lcom/alibaba/fastjson/parser/deserializer/ASMJavaBeanDeserializer$InnerJavaBeanDeserializer;");
        mw.visitMethodInsn(182, "com/alibaba/fastjson/parser/deserializer/JavaBeanDeserializer", "getFieldDeserializerMap", "()Ljava/util/Map;");
        mw.visitInsn(87);
        size = context.getFieldInfoList().size();
        for (i = 0; i < size; i++) {
            fieldInfo = (FieldInfo) context.getFieldInfoList().get(i);
            mw.visitVarInsn(25, 0);
            mw.visitLdcInsn(a.e + fieldInfo.getName() + "\":");
            mw.visitMethodInsn(182, "java/lang/String", "toCharArray", "()[C");
            mw.visitFieldInsn(181, context.getClassName(), fieldInfo.getName() + "_asm_prefix__", "[C");
        }
        mw.visitInsn(177);
        mw.visitMaxs(4, 4);
        mw.visitEnd();
    }

    private void _createInstance(ClassWriter cw, Context context) {
        MethodVisitor mw = cw.visitMethod(1, "createInstance", "(Lcom/alibaba/fastjson/parser/DefaultJSONParser;Ljava/lang/reflect/Type;)Ljava/lang/Object;", null, null);
        mw.visitTypeInsn(187, ASMUtils.getType(context.getClazz()));
        mw.visitInsn(89);
        mw.visitMethodInsn(183, ASMUtils.getType(context.getClazz()), "<init>", "()V");
        mw.visitInsn(176);
        mw.visitMaxs(3, 3);
        mw.visitEnd();
    }
}
