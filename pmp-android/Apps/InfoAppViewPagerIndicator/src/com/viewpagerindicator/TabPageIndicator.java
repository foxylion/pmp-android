/*
 * Copyright 2012 pmp-android development team
 * Project: InfoAppViewPagerIndicator
 * Project-Site: http://code.google.com/p/pmp-android/
 *
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.viewpagerindicator;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * This widget implements the dynamic action bar tab behavior that can change
 * across different configurations or circumstances.
 */
public class TabPageIndicator extends HorizontalScrollView implements PageIndicator {
    
    Runnable mTabSelector;
    
    private OnClickListener mTabClickListener = new OnClickListener() {
        
        public void onClick(View view) {
            TabView tabView = (TabView) view;
            TabPageIndicator.this.mViewPager.setCurrentItem(tabView.getIndex());
        }
    };
    
    private LinearLayout mTabLayout;
    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mListener;
    
    private LayoutInflater mInflater;
    
    int mMaxTabWidth;
    private int mSelectedTabIndex;
    
    
    public TabPageIndicator(Context context) {
        this(context, null);
    }
    
    
    public TabPageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        setHorizontalScrollBarEnabled(false);
        
        this.mInflater = LayoutInflater.from(context);
        
        this.mTabLayout = new LinearLayout(getContext());
        addView(this.mTabLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.FILL_PARENT));
    }
    
    
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final boolean lockedExpanded = widthMode == MeasureSpec.EXACTLY;
        setFillViewport(lockedExpanded);
        
        final int childCount = this.mTabLayout.getChildCount();
        if (childCount > 1 && (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST)) {
            if (childCount > 2) {
                this.mMaxTabWidth = (int) (MeasureSpec.getSize(widthMeasureSpec) * 0.4f);
            } else {
                this.mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
            }
        } else {
            this.mMaxTabWidth = -1;
        }
        
        final int oldWidth = getMeasuredWidth();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int newWidth = getMeasuredWidth();
        
        if (lockedExpanded && oldWidth != newWidth) {
            // Recenter the tab display if we're at a new (scrollable) size.
            setCurrentItem(this.mSelectedTabIndex);
        }
    }
    
    
    private void animateToTab(final int position) {
        final View tabView = this.mTabLayout.getChildAt(position);
        if (this.mTabSelector != null) {
            removeCallbacks(this.mTabSelector);
        }
        this.mTabSelector = new Runnable() {
            
            public void run() {
                final int scrollPos = tabView.getLeft() - (getWidth() - tabView.getWidth()) / 2;
                smoothScrollTo(scrollPos, 0);
                TabPageIndicator.this.mTabSelector = null;
            }
        };
        post(this.mTabSelector);
    }
    
    
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mTabSelector != null) {
            // Re-post the selector we saved
            post(this.mTabSelector);
        }
    }
    
    
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mTabSelector != null) {
            removeCallbacks(this.mTabSelector);
        }
    }
    
    
    private void addTab(String text, int index) {
        //Workaround for not being able to pass a defStyle on pre-3.0
        final TabView tabView = (TabView) this.mInflater.inflate(R.layout.vpi__tab, null);
        tabView.init(this, text, index);
        tabView.setFocusable(true);
        tabView.setOnClickListener(this.mTabClickListener);
        
        this.mTabLayout.addView(tabView, new LinearLayout.LayoutParams(0,
                android.view.ViewGroup.LayoutParams.FILL_PARENT, 1));
    }
    
    
    public void onPageScrollStateChanged(int arg0) {
        if (this.mListener != null) {
            this.mListener.onPageScrollStateChanged(arg0);
        }
    }
    
    
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        if (this.mListener != null) {
            this.mListener.onPageScrolled(arg0, arg1, arg2);
        }
    }
    
    
    public void onPageSelected(int arg0) {
        setCurrentItem(arg0);
        if (this.mListener != null) {
            this.mListener.onPageSelected(arg0);
        }
    }
    
    
    public void setViewPager(ViewPager view) {
        final PagerAdapter adapter = view.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        if (!(adapter instanceof TitleProvider)) {
            throw new IllegalStateException(
                    "ViewPager adapter must implement TitleProvider to be used with TitlePageIndicator.");
        }
        this.mViewPager = view;
        view.setOnPageChangeListener(this);
        notifyDataSetChanged();
    }
    
    
    public void notifyDataSetChanged() {
        this.mTabLayout.removeAllViews();
        TitleProvider adapter = (TitleProvider) this.mViewPager.getAdapter();
        final int count = ((PagerAdapter) adapter).getCount();
        for (int i = 0; i < count; i++) {
            addTab(adapter.getTitle(i), i);
        }
        if (this.mSelectedTabIndex > count) {
            this.mSelectedTabIndex = count - 1;
        }
        setCurrentItem(this.mSelectedTabIndex);
        requestLayout();
    }
    
    
    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }
    
    
    public void setCurrentItem(int item) {
        if (this.mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        this.mSelectedTabIndex = item;
        final int tabCount = this.mTabLayout.getChildCount();
        for (int i = 0; i < tabCount; i++) {
            final View child = this.mTabLayout.getChildAt(i);
            final boolean isSelected = (i == item);
            child.setSelected(isSelected);
            if (isSelected) {
                animateToTab(item);
            }
        }
    }
    
    
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.mListener = listener;
    }
    
    public static class TabView extends LinearLayout {
        
        private TabPageIndicator mParent;
        private int mIndex;
        
        
        public TabView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }
        
        
        public void init(TabPageIndicator parent, String text, int index) {
            this.mParent = parent;
            this.mIndex = index;
            
            TextView textView = (TextView) findViewById(android.R.id.text1);
            textView.setText(text);
        }
        
        
        @Override
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            
            // Re-measure if we went beyond our maximum size.
            if (this.mParent.mMaxTabWidth > 0 && getMeasuredWidth() > this.mParent.mMaxTabWidth) {
                super.onMeasure(MeasureSpec.makeMeasureSpec(this.mParent.mMaxTabWidth, MeasureSpec.EXACTLY),
                        heightMeasureSpec);
            }
        }
        
        
        public int getIndex() {
            return this.mIndex;
        }
    }
}
