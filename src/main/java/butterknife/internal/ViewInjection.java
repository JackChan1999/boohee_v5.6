package butterknife.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class ViewInjection {
    private final int id;
    private final LinkedHashMap<ListenerClass, Map<ListenerMethod, Set<ListenerBinding>>> listenerBindings = new LinkedHashMap();
    private final Set<ViewBinding> viewBindings = new LinkedHashSet();

    ViewInjection(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public Collection<ViewBinding> getViewBindings() {
        return this.viewBindings;
    }

    public Map<ListenerClass, Map<ListenerMethod, Set<ListenerBinding>>> getListenerBindings() {
        return this.listenerBindings;
    }

    public boolean hasListenerBinding(ListenerClass listener, ListenerMethod method) {
        Map<ListenerMethod, Set<ListenerBinding>> methods = (Map) this.listenerBindings.get(listener);
        return methods != null && methods.containsKey(method);
    }

    public void addListenerBinding(ListenerClass listener, ListenerMethod method, ListenerBinding binding) {
        Map<ListenerMethod, Set<ListenerBinding>> methods = (Map) this.listenerBindings.get(listener);
        Set<ListenerBinding> set = null;
        if (methods == null) {
            methods = new LinkedHashMap();
            this.listenerBindings.put(listener, methods);
        } else {
            set = (Set) methods.get(method);
        }
        if (set == null) {
            set = new LinkedHashSet();
            methods.put(method, set);
        }
        set.add(binding);
    }

    public void addViewBinding(ViewBinding viewBinding) {
        this.viewBindings.add(viewBinding);
    }

    public List<Binding> getRequiredBindings() {
        List<Binding> requiredBindings = new ArrayList();
        for (ViewBinding viewBinding : this.viewBindings) {
            if (viewBinding.isRequired()) {
                requiredBindings.add(viewBinding);
            }
        }
        for (Map<ListenerMethod, Set<ListenerBinding>> methodBinding : this.listenerBindings.values()) {
            for (Set<ListenerBinding> set : methodBinding.values()) {
                for (ListenerBinding binding : set) {
                    if (binding.isRequired()) {
                        requiredBindings.add(binding);
                    }
                }
            }
        }
        return requiredBindings;
    }
}
