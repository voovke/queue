package com.example.osemenenko.queue;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity {

    public static final String FIRST_SERVICE_ID = "S1";
    public static final String SECOND_SERVICE_ID = "S2";

    public static final String NUMBER_FORMAT = "%s-%s";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        defineButtonActionListener(FIRST_SERVICE_ID, R.id.ServiceButtonOne);
        defineButtonActionListener(SECOND_SERVICE_ID, R.id.ServiceButtonTwo);

    }


    private void defineButtonActionListener(final String serviceId, final int widgetId) {
        final Button button = findViewById(widgetId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewQueueItem(serviceId);
                updateWithLastServiceNumber();
            }
        });
    }

    private void updateWithLastServiceNumber() {


        try (Realm realm = Realm.getDefaultInstance()) {
            final TextView viewById = findViewById(R.id.viewNumber);
            final QueueModel queue = realm.where(QueueModel.class).sort(QueueModel.ID, Sort.DESCENDING).findFirst();
            final String formattedNumber;
            if (queue != null) {
                final String serviceId = queue.getServiceId();
                final long number = queue.getNumber();

                formattedNumber = String.format(NUMBER_FORMAT, serviceId, number);
            } else {
                formattedNumber = "";
            }

            viewById.setText(formattedNumber);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void createNewQueueItem(String serviceId) {
        try (Realm realm = Realm.getDefaultInstance()) {

            final QueueModel lastQueueItemByService = realm.where(QueueModel.class).contains(QueueModel.SERVICE_ID, serviceId)
                    .sort(QueueModel.NUMBER, Sort.DESCENDING).findFirst();

            final QueueModel lastQueueItem = realm.where(QueueModel.class)
                    .sort(QueueModel.ID, Sort.DESCENDING).findFirst();

            long newId = lastQueueItem != null ? lastQueueItem.getId() + 1 : 0L;

            long numberInQueue = lastQueueItemByService != null ? lastQueueItemByService.getNumber() + 1 : 0L;


            final QueueModel queueModel = new QueueModel();
            queueModel.setId(newId);
            queueModel.setNumber(numberInQueue);
            queueModel.setServiceId(serviceId);

            // Persist your data in a transaction
            realm.beginTransaction();
            realm.copyToRealm(queueModel);
            realm.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
