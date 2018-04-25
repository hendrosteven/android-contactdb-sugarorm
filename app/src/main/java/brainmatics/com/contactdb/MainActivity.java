package brainmatics.com.contactdb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import brainmatics.com.adapter.ContactAdapter;
import brainmatics.com.entity.Contact;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.listContact)
    ListView listContact;

    List<Contact> contacts = new ArrayList<Contact>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        registerForContextMenu(listContact);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_delete, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.mnuDelete:
                Contact contact = (Contact)listContact.getItemAtPosition(info.position);
                contact.delete();
                Toast.makeText(this,"Contact terhapus",Toast.LENGTH_LONG).show();
                loadAllContact();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @OnItemClick(R.id.listContact)
    public void listContactOnItemClick(int position){
        Contact contact = (Contact)listContact.getItemAtPosition(position);
        Intent intent = new Intent(this,EditActivity.class);
        intent.putExtra("ID",contact.getId());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllContact();
    }

    private void loadAllContact(){
        contacts = Contact.listAll(Contact.class);
        listContact.setAdapter(new ContactAdapter(this,contacts));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuAddContact:
                Intent intent = new Intent(this, InputActivity.class);
                startActivity(intent);
                break;
            case R.id.mnuInfo:
                Toast.makeText(this,"Info Aplikasi",Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
        return true;
    }
}
