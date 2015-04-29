package com.stone.myfragment;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.stone.myfragment.fragment.MyFrag1;
import com.stone.myfragment.fragment.MyFrag2;
import com.stone.myfragment.fragment.MyFrag3;

/**
 * 测试 FragmentTransaction 使用add方式 添加Fragment
 * 切换Fragment，使用show、hide方法。
 * 解决Fragment重叠问题
 */
public class FragActivity extends FragmentActivity {
	FragmentManager manager;
	FragmentTransaction transaction;
	
	MyFrag1 frag1;
	MyFrag2 frag2;
	MyFrag3 frag3;
	
	Fragment currentFrag;
	int index;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.frag_main);
		
		manager = getSupportFragmentManager();
		
		frag1 = new MyFrag1();
		frag2 = new MyFrag2();
		frag3 = new MyFrag3();

		display(frag1);
//		displayWithReplace(frag1);
		index = 1;

		System.out.println("oncreate--FragActivity");
	}

	public void myexp(View view) {
		startActivity(new Intent(this, PopExceptionActivity.class));
	}

	public void frag1(View view) {
		display(frag1);
		index = 1;
//		displayWithReplace(new MyFrag1());
	}
	
	public void frag2(View view) {
		display(frag2);
		index = 2;
//		displayWithReplace(new MyFrag2());
	}
	
	public void frag3(View view) {
		display(frag3);
		index = 3;
//		displayWithReplace(new MyFrag3());
	}
	/**
	 * displayFrag 要显示的fragment (使用 add show hide)
	 */
	private void display(Fragment displayFrag) {
		if (currentFrag != displayFrag) {
			transaction = manager.beginTransaction();
			if (displayFrag == null) return;

			if (displayFrag.isAdded()) {
				transaction.show(displayFrag);
			} else {
				transaction.add(R.id.fl_content, displayFrag);
			}

			if (currentFrag != null) {
				transaction.hide(currentFrag);
			}
			currentFrag = displayFrag;
			transaction.commit();
		}
		
	}
	/**
	 * displayFrag 要显示的fragment (使用 replace, 会销毁被替换的fragment)
	 */
	private void displayWithReplace(Fragment displayFrag) {
		if (currentFrag != displayFrag) {
			transaction = manager.beginTransaction();
			if (displayFrag == null) return;

			transaction.replace(R.id.fl_content, displayFrag);

			if (currentFrag != null) {
				transaction.hide(currentFrag);
			}
			currentFrag = displayFrag;
			transaction.commit();
		}

	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		/*
		当 因某些原因导致activity重新调用 oncreate时，
		该状态outState中，会更新fragment相关的状态。导致可能会覆盖fragment的情况
		所以 注释掉super方法
		 */
//		super.onSaveInstanceState(outState);
		System.out.println("onSaveInstanceState");
		outState.putInt("index", index);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		System.out.println("onRestoreInstanceState");
		int savedIndex = savedInstanceState.getInt("index");
		if (savedIndex != this.index) {//activity oncreate后 this.index一定=1
			if (frag1.isAdded()) {
				manager.beginTransaction().hide(frag1).commit();
			}
			if (savedIndex == 2) {
				display(frag2);
			} else if (savedIndex == 3){
				display(frag3);
			}

		}
	}
}
