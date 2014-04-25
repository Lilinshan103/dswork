package dswork.android.demo.framework.app.single;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import dswork.android.R;
import dswork.android.R.layout;
import dswork.android.R.menu;
import dswork.android.controller.DemoController;
import dswork.android.controller.PersonController;
import dswork.android.util.InjectUtil;
import dswork.android.util.MyStrictMode;
import dswork.android.util.InjectUtil.InjectView;
import dswork.android.view.OleActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class PersonSearchActivity extends OleActivity
{
	@InjectView(id=R.id.name) EditText name;//姓名
	@InjectView(id=R.id.phone) EditText phone;//电话
	@InjectView(id=R.id.amount) EditText amount;//存款
	@InjectView(id=R.id.btn_search) Button btnSearch;//搜索按钮
	PersonController controller;
	
	@Override
	public void initMainView() 
	{
		MyStrictMode.setPolicy();//webapp需要调用此方法
		setContentView(R.layout.activity_person_search);
		InjectUtil.injectView(this);//注入控件
		controller = new PersonController(this);
		btnSearch.setOnClickListener(new SearchClickListener());
	}

	@Override
	public void initMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.person_search, menu);
	}
	
	private final class SearchClickListener implements OnClickListener
	{
		@Override
		public void onClick(View v) 
		{
			search(v);
		}
	}
	
	public void search(View v)
	{
		Map m = new HashMap();
		m.put("name", name.getText().toString().trim());
		m.put("phone", phone.getText().toString().trim());
		m.put("amount", amount.getText().toString().trim());
		List<Map<String,Object>> params = new ArrayList<Map<String,Object>>();
		params.add(m);
		this.finish();
		startActivity(new Intent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setClass(this, PersonActivity.class).putExtra("params", (Serializable)params));
	}
}
