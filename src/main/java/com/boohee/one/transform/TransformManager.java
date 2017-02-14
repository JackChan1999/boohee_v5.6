package com.boohee.one.transform;

import com.ToxicBakery.viewpager.transforms.ABaseTransformer;
import com.ToxicBakery.viewpager.transforms.BackgroundToForegroundTransformer;
import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.ToxicBakery.viewpager.transforms.ForegroundToBackgroundTransformer;
import com.ToxicBakery.viewpager.transforms.RotateDownTransformer;
import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.ToxicBakery.viewpager.transforms.TabletTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomInTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomOutSlideTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomOutTranformer;

import java.util.Random;

public class TransformManager {
    public static final ABaseTransformer[] transfroms = new ABaseTransformer[]{new
            BackgroundToForegroundTransformer(), new ForegroundToBackgroundTransformer(), new
            CubeOutTransformer(), new RotateDownTransformer(), new RotateUpTransformer(), new
            TabletTransformer(), new ZoomInTransformer(), new ZoomOutTranformer(), new
            ZoomOutSlideTransformer()};

    public static ABaseTransformer getRandomTransform() {
        return transfroms[new Random().nextInt(transfroms.length)];
    }
}
