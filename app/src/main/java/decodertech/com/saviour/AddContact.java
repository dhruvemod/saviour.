package decodertech.com.saviour;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static decodertech.com.saviour.ContactDb.Contact_person.COLUMN_LOCATION;
import static decodertech.com.saviour.ContactDb.Contact_person.COLUMN_NAME;
import static decodertech.com.saviour.ContactDb.Contact_person.COLUMN_NUMBER;
import static decodertech.com.saviour.ContactDb.Contact_person.TABLE_NAME;

public class AddContact extends AppCompatActivity {
    EditText name,number,location;
    private static final int RESULT_PICK_CONTACT = 855;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        Button button= (Button) findViewById(R.id.submit);
        name= (EditText) findViewById(R.id.nameEditText);
        number=(EditText)findViewById(R.id.numberEditText);
        location=(EditText)findViewById(R.id.locationEditText);
        Button importContact= (Button) findViewById(R.id.importContactButton);
        importContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickContact(view);

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveContact();
             finish();
            }
        });
    }
    private void saveContact(){
        SQLiteDatabase database=new SqliteHelper(this).getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        try {
            contentValues.put(COLUMN_NAME, name.getText().toString());
            contentValues.put(COLUMN_NUMBER, number.getText().toString());
            contentValues.put(COLUMN_LOCATION, location.getText().toString());
            long newrowid = database.insert(TABLE_NAME, null, contentValues);
        }catch (Exception e){

        }
    }
    public void pickContact(View view){
        Intent contactPickIntent=new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPickIntent,RESULT_PICK_CONTACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK){
            switch (requestCode){
                case RESULT_PICK_CONTACT:

                    contactPicked(data);

                    break;
            }
        }
        else{
            Toast.makeText(getApplicationContext(),"failed",Toast.LENGTH_SHORT).show();
        }
    }
    private void contactPicked(Intent data){
        String phone_number=null;
        String nam=null;
        Cursor cursor=null;
        try{


            Uri uri=data.getData();cursor=getContentResolver().query(uri,null,null,null,null);
            cursor.moveToFirst();
            int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            phone_number=cursor.getString(phoneIndex);
            nam=cursor.getString(nameIndex);
            name.setText(nam, TextView.BufferType.EDITABLE);
            number.setText(phone_number, TextView.BufferType.EDITABLE);

        }catch (Exception e){
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
