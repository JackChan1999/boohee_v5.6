package me.nereo.multi_image_selector;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.io.File;
import me.nereo.multi_image_selector.bean.Image;
import me.nereo.multi_image_selector.utils.MultiImageLoader;
import me.nereo.multi_image_selector.utils.MultiImageLoader.LoadCallBack;
import me.nereo.multi_image_selector.utils.MultiImageSelector;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher$OnPhotoTapListener;

public class MultiImagePreviewFragment extends Fragment {
    private static final String KEY_PHOTO = "key_photo";
    PhotoViewAttacher attacher;
    Image image;
    PhotoView ivPhoto;
    MultiImageLoader mImageLoader;

    public static MultiImagePreviewFragment newInstance(Image image) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_PHOTO, image);
        MultiImagePreviewFragment fragment = new MultiImagePreviewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.image = (Image) bundle.getParcelable(KEY_PHOTO);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_multi_image_preview, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView();
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mImageLoader = MultiImageSelector.getImageLoader();
        init();
    }

    private void findView() {
        View v = getView();
        if (v != null) {
            this.ivPhoto = (PhotoView) v.findViewById(R.id.iv_photo);
        }
    }

    private void init() {
        if (this.mImageLoader != null) {
            this.attacher = new PhotoViewAttacher(this.ivPhoto);
            this.attacher.setOnPhotoTapListener(new PhotoViewAttacher$OnPhotoTapListener() {
                public void onPhotoTap(View view, float v, float v2) {
                    MultiImagePreviewFragment.this.getActivity().finish();
                }
            });
            this.mImageLoader.loadImage(Uri.decode(Uri.fromFile(new File(this.image.path)).toString()), this.ivPhoto, 17170445, new LoadCallBack() {
                public void onSuccess() {
                    if (MultiImagePreviewFragment.this.attacher != null) {
                        MultiImagePreviewFragment.this.attacher.update();
                    }
                }
            });
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.attacher != null) {
            this.attacher.cleanup();
        }
    }
}
