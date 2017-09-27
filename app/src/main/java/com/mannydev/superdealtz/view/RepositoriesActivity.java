package com.mannydev.superdealtz.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mannydev.superdealtz.R;
import com.mannydev.superdealtz.controller.Controller;
import com.mannydev.superdealtz.controller.adapters.RepositoryAdapter;
import com.mannydev.superdealtz.model.Repository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Активити для отображения списка репозиториев организации на GitHub
 */

public class RepositoriesActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txtTitle;
    ListView listRepos;
    private String organization_link, org;

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repositories);

        //Получаем данные из MainActivity
        Intent intent = getIntent();
        org = intent.getStringExtra("orgName");
        organization_link = intent.getStringExtra("organization");

        //Инициализируем объекты View
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtTitle = (TextView)findViewById(R.id.txtTitle);
        listRepos = (ListView)findViewById(R.id.listRepos);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        setSupportActionBar(toolbar);
        txtTitle.setText(org+" "+txtTitle.getText());

        // Добавляем кнопку "Назад"
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        //Запускаем поиск репозиториев
        FindRepositories findRepositories = new FindRepositories();
        findRepositories.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    //Класс для работы в другом потоке
    class FindRepositories extends AsyncTask<Void,Void,ArrayList<Repository>> {

        ArrayList<Repository> repList;
        String organization;
        String resultJson = "";
        String link1 = "https://api.github.com/orgs/";
        String link2 = "/repos?page=";
        String link3 = "&per_page=100";

        @Override
        protected void onPreExecute(){
            repList = new ArrayList<>();

            organization = organization_link;
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Repository> doInBackground(Void... noargs) {

            //Пройдемся циклом по страницам GitHub
            for (int i=1;i<=20;i++){
                resultJson="";
                String link = link1+organization+link2+i+link3; //Собираем нашу ссылку
                Controller controller = new Controller();
                InputStream inputStream = null;
                try {
                    inputStream = controller.getInputStream(link);
                    resultJson = controller.getJSONFromStream(inputStream);

                    //Если пошли пустые страницы - прерываем цикл
                    if (resultJson.equals("[]")){
                        return repList;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.v("myLogs","Ошибка чтения ссылки "+e);
                    return null;
                }
                try {
                    JSONArray repositories = new JSONArray(resultJson);
                    for (int j=0;j<repositories.length();j++){
                        JSONObject rep = repositories.getJSONObject(j);
                        Repository repository = new Repository();
                        repository.setName(rep.getString("name"));
                        repository.setDescription(rep.getString("description"));
                        repList.add(repository);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }

            return repList;

        }

        @Override
        protected void onPostExecute(ArrayList<Repository> result) {
            progressBar.setVisibility(View.INVISIBLE);

            //Выводим результат на экран
            final RepositoryAdapter repositoryAdapter =
                    new RepositoryAdapter(RepositoriesActivity.this,result);
            listRepos.setAdapter(repositoryAdapter);
            listRepos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Repository rep = repositoryAdapter.getRepository(i);
                    String name = rep.getName();
                    String description = rep.getDescription();
                    final MaterialDialog materialDialog = new MaterialDialog(RepositoriesActivity.this);
                    materialDialog.setTitle(org);
                    materialDialog.setMessage("Репозиторий : " + name + "\n" + "Описание : " + "\n"
                            + description);
                    materialDialog.setPositiveButton("OK", new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            materialDialog.dismiss();
                        }
                    });
                    materialDialog.show();
                }
            });
            txtTitle.setText(txtTitle.getText()+" ("+repositoryAdapter.getCount()+")");
        }
    }
}
