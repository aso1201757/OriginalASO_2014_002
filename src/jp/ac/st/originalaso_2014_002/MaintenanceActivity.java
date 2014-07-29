package jp.ac.st.originalaso_2014_002;

import android.app.Activity;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MaintenanceActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener{

	SQLiteDatabase sdb = null;
	MySQLiteOpenHelper helper = null;

	int selectedID = -1;
	int lastPosition = -1;




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maintenanceactivity);
	}




	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO 自動生成されたメソッド・スタブ
		if(this.selectedID!=-1){
			parent.getChildAt(this.lastPosition).setBackgroundColor(0);
		}
		view.setBackgroundColor(android.graphics.Color.LTGRAY);

		SQLiteCursor cursor = (SQLiteCursor)parent.getItemAtPosition(position);
		this.selectedID = cursor.getInt(cursor.getColumnIndex("_id"));
		this.lastPosition = position;
	}



	@Override
	protected void onResume() {
		// TODO 自動生成されたメソッド・スタブ
		super.onResume();

		Button btnD = (Button)findViewById(R.id.btndelete);
		Button btnB = (Button)findViewById(R.id.btnback);
		ListView lstH = (ListView)findViewById(R.id.lsthitokoto);

		btnD.setOnClickListener(this);
		btnB.setOnClickListener(this);

		lstH.setOnItemClickListener(this);

		this.setDBValuetoList(lstH);
	}

	private void setDBValuetoList(ListView lstH) {
		// TODO 自動生成されたメソッド・スタブ
		SQLiteCursor cursor = null;

		if(sdb == null) {
			helper = new MySQLiteOpenHelper(getApplicationContext());
		}
		try{
			sdb = helper.getWritableDatabase();
		}catch(SQLiteException e){
			Log.e("ERROR",e.toString());
		}
		cursor = this.helper.selectHitokotoList(sdb);

		int db_layout = android.R.layout.simple_list_item_activated_1;
		String[]from = {"phrase"};
		int[] to = new int[]{android.R.id.text1};

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,db_layout,cursor,from,to,0);

		lstH.setAdapter(adapter);
	}

	private void deleteFromHitokoto(int id){
		if(sdb == null)	{
			helper = new MySQLiteOpenHelper(getApplicationContext());
		}
		try{
			sdb = helper.getWritableDatabase();
		}catch(SQLiteException e){
			Log.e("ERROR", e.toString());
		}
		this.helper.deleteHitokoto(sdb,  id);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.btndelete:
	
				if(this.selectedID != -1){
					this.deleteFromHitokoto(this.selectedID);
					ListView lstH  = (ListView)findViewById(R.id.lsthitokoto);
					this.setDBValuetoList(lstH);
					this.selectedID = -1;
					this.lastPosition = -1;
				}else{
					Toast.makeText(MaintenanceActivity.this, "消去する行を選んでください",Toast.LENGTH_SHORT).show();
				}
			break;
			case R.id.btnback:
				finish();
			break;
			// TODO 自動生成されたメソッド・スタブ
		}
	}

}


