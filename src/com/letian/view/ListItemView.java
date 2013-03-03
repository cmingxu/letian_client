package com.letian.view;

import com.letian.R;

import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListItemView extends LinearLayout {

	public ListItemView(Context context, String title, String words,
			boolean green) {
		super(context);

		this.setOrientation(HORIZONTAL);
		this.setPadding(5, 5, 5, 5);

		this.setGravity(Gravity.CENTER_VERTICAL);
		image_view = new ImageView(context);
		if (green) {
			image_view.setImageDrawable(this.getResources().getDrawable(
					R.drawable.green));
		} else {
			image_view.setImageDrawable(this.getResources().getDrawable(
					R.drawable.red));
		}
	
	
	
		image_view.setPadding(0, 0, 20, 0);
		
		layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		addView(image_view, new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		
		addView(layout, new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		mTitle = new TextView(context);
		mTitle.setText(title);
		layout.addView(mTitle, new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

		mDialogue = new TextView(context);
		mDialogue.setText(words);
		layout.addView(mDialogue, new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
	}



	private TextView mTitle;
	private TextView mDialogue;
	private ImageView image_view;
	private LinearLayout layout;
}
