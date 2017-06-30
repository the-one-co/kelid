package ir.mehdi.kelid.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

import ir.mehdi.kelid.Constant;

import ir.mehdi.kelid.R;
import ir.mehdi.kelid.ui.MainActivity;
import ir.mehdi.kelid.ui.ShowInfoActivity;

/**
 * Created by admin on 24/06/2017.
 */

public class HomeFragment extends Fragment implements Constant {
    private ViewPager mViewPager;
    Button showInfo;
    private AdversSliderAdapter adversSliderAdapter;
    int[] colors=new int[]{Color.RED,Color.BLUE,Color.YELLOW};
    CirclePageIndicator circlePageIndicator;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,null);
        adversSliderAdapter=new AdversSliderAdapter(getActivity());
        mViewPager= (ViewPager) view.findViewById(R.id.container);
        showInfo= (Button) view.findViewById(R.id.button3);
        mViewPager.setAdapter(adversSliderAdapter);
        circlePageIndicator = (CirclePageIndicator) view.findViewById(R.id.viewpagerindicator);


        circlePageIndicator.setViewPager(mViewPager);
        showInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(getActivity(), ShowInfoActivity.class);
                getActivity().startActivity(a);
            }
        });
//        view.setBackgroundColor(Color.BLACK);
        return view;
    }

    class AdversSliderAdapter extends PagerAdapter {


        private LayoutInflater inflater;
        private Context context;

        public AdversSliderAdapter(Context context) {
            this.context = context;

            inflater = LayoutInflater.from(context);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return colors.length;
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            View myImageLayout = inflater.inflate(R.layout.fragment_image_slider, view, false);
            ImageView myImage = (ImageView) myImageLayout
                    .findViewById(R.id.image);
            myImage.setBackgroundColor(colors[position]);

//            myImage.setImageResource(images.get(position));
            view.addView(myImageLayout, 0);
            return myImageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }
    }
}
