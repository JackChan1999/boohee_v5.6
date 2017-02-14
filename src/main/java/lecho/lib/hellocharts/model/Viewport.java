package lecho.lib.hellocharts.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Viewport implements Parcelable {
    public static final Creator<Viewport> CREATOR = new Creator<Viewport>() {
        public Viewport createFromParcel(Parcel in) {
            Viewport v = new Viewport();
            v.readFromParcel(in);
            return v;
        }

        public Viewport[] newArray(int size) {
            return new Viewport[size];
        }
    };
    public float bottom;
    public float left;
    public float right;
    public float top;

    public Viewport(float left, float top, float right, float bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public Viewport(Viewport v) {
        if (v == null) {
            this.bottom = 0.0f;
            this.right = 0.0f;
            this.top = 0.0f;
            this.left = 0.0f;
            return;
        }
        this.left = v.left;
        this.top = v.top;
        this.right = v.right;
        this.bottom = v.bottom;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Viewport other = (Viewport) obj;
        if (Float.floatToIntBits(this.bottom) != Float.floatToIntBits(other.bottom)) {
            return false;
        }
        if (Float.floatToIntBits(this.left) != Float.floatToIntBits(other.left)) {
            return false;
        }
        if (Float.floatToIntBits(this.right) != Float.floatToIntBits(other.right)) {
            return false;
        }
        if (Float.floatToIntBits(this.top) != Float.floatToIntBits(other.top)) {
            return false;
        }
        return true;
    }

    public final boolean isEmpty() {
        return this.left >= this.right || this.bottom >= this.top;
    }

    public void setEmpty() {
        this.bottom = 0.0f;
        this.top = 0.0f;
        this.right = 0.0f;
        this.left = 0.0f;
    }

    public final float width() {
        return this.right - this.left;
    }

    public final float height() {
        return this.top - this.bottom;
    }

    public final float centerX() {
        return (this.left + this.right) * 0.5f;
    }

    public final float centerY() {
        return (this.top + this.bottom) * 0.5f;
    }

    public void set(float left, float top, float right, float bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public void set(Viewport src) {
        this.left = src.left;
        this.top = src.top;
        this.right = src.right;
        this.bottom = src.bottom;
    }

    public void offset(float dx, float dy) {
        this.left += dx;
        this.top += dy;
        this.right += dx;
        this.bottom += dy;
    }

    public void offsetTo(float newLeft, float newTop) {
        this.right += newLeft - this.left;
        this.bottom += newTop - this.top;
        this.left = newLeft;
        this.top = newTop;
    }

    public void inset(float dx, float dy) {
        this.left += dx;
        this.top -= dy;
        this.right -= dx;
        this.bottom += dy;
    }

    public boolean contains(float x, float y) {
        return this.left < this.right && this.bottom < this.top && x >= this.left && x < this.right && y >= this.bottom && y < this.top;
    }

    public boolean contains(float left, float top, float right, float bottom) {
        return this.left < this.right && this.bottom < this.top && this.left <= left && this.top >= top && this.right >= right && this.bottom <= bottom;
    }

    public boolean contains(Viewport v) {
        return this.left < this.right && this.bottom < this.top && this.left <= v.left && this.top >= v.top && this.right >= v.right && this.bottom <= v.bottom;
    }

    public void union(float left, float top, float right, float bottom) {
        if (left < right && bottom < top) {
            if (this.left >= this.right || this.bottom >= this.top) {
                this.left = left;
                this.top = top;
                this.right = right;
                this.bottom = bottom;
                return;
            }
            if (this.left > left) {
                this.left = left;
            }
            if (this.top < top) {
                this.top = top;
            }
            if (this.right < right) {
                this.right = right;
            }
            if (this.bottom > bottom) {
                this.bottom = bottom;
            }
        }
    }

    public void union(Viewport v) {
        union(v.left, v.top, v.right, v.bottom);
    }

    public boolean intersect(float left, float top, float right, float bottom) {
        if (this.left >= right || left >= this.right || this.bottom >= top || bottom >= this.top) {
            return false;
        }
        if (this.left < left) {
            this.left = left;
        }
        if (this.top > top) {
            this.top = top;
        }
        if (this.right > right) {
            this.right = right;
        }
        if (this.bottom < bottom) {
            this.bottom = bottom;
        }
        return true;
    }

    public boolean intersect(Viewport v) {
        return intersect(v.left, v.top, v.right, v.bottom);
    }

    public String toString() {
        return "Viewport [left=" + this.left + ", top=" + this.top + ", right=" + this.right + ", bottom=" + this.bottom + "]";
    }

    public int hashCode() {
        return ((((((Float.floatToIntBits(this.bottom) + 31) * 31) + Float.floatToIntBits(this.left)) * 31) + Float.floatToIntBits(this.right)) * 31) + Float.floatToIntBits(this.top);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeFloat(this.left);
        out.writeFloat(this.top);
        out.writeFloat(this.right);
        out.writeFloat(this.bottom);
    }

    public void readFromParcel(Parcel in) {
        this.left = in.readFloat();
        this.top = in.readFloat();
        this.right = in.readFloat();
        this.bottom = in.readFloat();
    }
}
