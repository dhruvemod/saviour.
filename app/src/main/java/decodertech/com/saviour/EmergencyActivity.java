package decodertech.com.saviour;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static decodertech.com.saviour.ContactDb.Contact_person.TABLE_NAME;

/**
 * Created by Dhruve on 9/18/2017.
 */

public class EmergencyActivity extends Fragment  {

    CustomAdapter customAdapter;
    ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_emergency, container, false);
        listView=(ListView) rootView.findViewById(R.id.listViewTHis);

        FloatingActionButton floatingActionButton=rootView.findViewById(R.id.fab);
        ColorStateList csl = new ColorStateList(new int[][]{new int[0]}, new int[]{0xffff0000});
        floatingActionButton.setBackgroundTintList(csl);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                contact c=customAdapter.getItem(i);
                String numb=c.getNumber();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+numb));

                startActivity(intent);
            }
        });



        try{


        UI(listView);



        }catch (Exception e){

        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),AddContact.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });
        return rootView;
    }
public List<contact> readFromDb(){

    SQLiteDatabase database=new SqliteHelper(getContext()).getReadableDatabase();
    String selectQuery="SELECT  * FROM " + TABLE_NAME;
    ArrayList<contact> arrayList=new ArrayList<>();
    Cursor cursor = database.rawQuery(selectQuery, null);
    if (cursor.moveToFirst()) {
        do {
            contact lang = new contact();
            lang.setPerson_name(cursor.getString(1));
            lang.setNumber(cursor.getString(2));
            lang.setLoc(cursor.getString(3));
            arrayList.add(lang);
        } while (cursor.moveToNext());
    }

    return arrayList;

}
public void UI(ListView listView){
    List<contact> arrayList= readFromDb();
    customAdapter=new CustomAdapter(getContext(),arrayList);
    listView.setAdapter(customAdapter);
}

    @Override
    public void onResume() {
        super.onResume();
        UI(listView);
    }
}
