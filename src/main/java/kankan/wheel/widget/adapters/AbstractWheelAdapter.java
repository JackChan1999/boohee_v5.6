package kankan.wheel.widget.adapters;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractWheelAdapter implements WheelViewAdapter {
    private List<DataSetObserver> datasetObservers;

    public View getEmptyItem(View convertView, ViewGroup parent) {
        return null;
    }

    public void registerDataSetObserver(DataSetObserver observer) {
        if (this.datasetObservers == null) {
            this.datasetObservers = new LinkedList();
        }
        this.datasetObservers.add(observer);
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
        if (this.datasetObservers != null) {
            this.datasetObservers.remove(observer);
        }
    }

    protected void notifyDataChangedEvent() {
        if (this.datasetObservers != null) {
            for (DataSetObserver observer : this.datasetObservers) {
                observer.onChanged();
            }
        }
    }

    protected void notifyDataInvalidatedEvent() {
        if (this.datasetObservers != null) {
            for (DataSetObserver observer : this.datasetObservers) {
                observer.onInvalidated();
            }
        }
    }
}
