package com.mannydev.superdealtz.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mannydev.superdealtz.R;
import com.mannydev.superdealtz.controller.adapters.NullAdapter;
import com.mannydev.superdealtz.controller.adapters.OrganizationAdapter;
import com.mannydev.superdealtz.controller.Controller;
import com.mannydev.superdealtz.model.Organization;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Активити для поиска организации на GitHub
 */

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView txtNull;
    ListView lvMain;
    ProgressBar pBar;
    ArrayList<Organization> organizationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Инициализируем объекты
        editText = (EditText)findViewById(R.id.editText);
        lvMain = (ListView)findViewById(R.id.lvMain);
        txtNull = (TextView)findViewById(R.id.txtNull);
        pBar = (ProgressBar)findViewById(R.id.progressBar4);
        pBar.setVisibility(View.INVISIBLE);
        organizationList = new ArrayList<>();

        //Добавляем слушателя на изменение EditText
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //не нужен
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>=3||charSequence.length()<1){
                    //Запускаем процесс поиска
                    FindOrganization findOrganization = new FindOrganization();
                    findOrganization.execute();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //не нужен
            }
        });
    }

    //Класс для работы в другом потоке
    class FindOrganization extends AsyncTask<Void,Void,Organization>{

        String organization;
        JSONObject jsonObject;
        String resultJson = "";
        String link = "https://api.github.com/orgs/";

        @Override
        protected void onPreExecute(){

            //Получаем данные введенные пользователем чтобы подставить в ссылку
            organization = editText.getText().toString();
            pBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected Organization doInBackground(Void... noargs) {
            Controller controller = new Controller();
            try {
                InputStream inputStream = controller.getInputStream(link+organization);
                resultJson = controller.getJSONFromStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
                Log.v("myLogs","Ошибка чтения ссылки "+e);
                return null;
            }
            if (resultJson.equals("")==false){
                Organization orgFromJSON = new Organization();
                try {
                    jsonObject = new JSONObject(resultJson);
                    orgFromJSON.setName(jsonObject.getString("name"));
                    orgFromJSON.setLocation(jsonObject.getString("location"));
                    orgFromJSON.setAvatar_url(jsonObject.getString("avatar_url"));
                    orgFromJSON.setBlog(jsonObject.getString("blog"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                fillEmptyFields(orgFromJSON);
                return orgFromJSON;
            }else
                return null;
        }

        @Override
        protected void onPostExecute(Organization result) {
            organizationList = new ArrayList<>();
            organizationList.add(result);
            pBar.setVisibility(View.INVISIBLE);
            if(result!=null){
                txtNull.setText("");
                final OrganizationAdapter adapter;
                 adapter = new OrganizationAdapter(MainActivity.this,organizationList);
                lvMain.setAdapter(adapter);
                //Слушатель на каждый элемент списка
                lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Organization org = adapter.getOrganization(i);
                        String orgName = org.getName();

                        //Передаем данные на RepositoryActivity
                        Intent intent = new Intent(MainActivity.this,RepositoriesActivity.class);
                        intent.putExtra("orgName", orgName); //для шляпы
                        intent.putExtra("organization",organization); //для дальнейших поисков
                        startActivity(intent);
                    }
                });
            }else {
                //Обнуляем список
                NullAdapter nullAdapter = new NullAdapter(MainActivity.this);
                lvMain.setAdapter(nullAdapter);
                txtNull.setText("Такой организации не найдено.");
            }

        }

        //Заполняем пробелы в информации
        public void fillEmptyFields(Organization organization){
            if(organization.getName()==null){
                organization.setName("No Name");
            }
            if(organization.getLocation()==null||organization.getLocation().equals("")==true){
                organization.setLocation("No Location");
            }
            if(organization.getAvatar_url()==null){
                organization.setAvatar_url("http://trade-drive.ru/upload/medialibrary/2e8/"
                                +"2e895d080c4036f91acf0aa00516649d.jpg");
            }
            if(organization.getBlog()==null||organization.getBlog().equals("")==true){
                organization.setBlog("No Blog");
            }
        }
    }
}
