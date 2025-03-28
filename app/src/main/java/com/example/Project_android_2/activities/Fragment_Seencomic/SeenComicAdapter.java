package com.example.Project_android_2.activities.Fragment_Seencomic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


public class SeenComicAdapter extends FragmentStatePagerAdapter {
    private String comicId;

    public SeenComicAdapter(@NonNull FragmentManager fm, int behavior, String comicId) {
        super(fm, behavior);
        this.comicId = comicId;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new IntroFragment().newInstance(comicId, null);
            case 1:
                return ListChapterFragment.newInstance(comicId, null);
            default:
                return new ListChapterFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Giới thiệu";
            case 1:
                return "D.S Chương";
            default:
                return "D.S Chương";
        }
    }
}
