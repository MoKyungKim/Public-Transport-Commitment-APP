package com.example.cooperativeproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amazonaws.amplify.generated.graphql.CreateTaskMutation;
import com.amazonaws.amplify.generated.graphql.ListTasksQuery;
import com.amazonaws.amplify.generated.graphql.OnCreateTaskSubscription;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.amazonaws.mobileconnectors.appsync.AppSyncSubscriptionCall;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.amazonaws.mobileconnectors.appsync.sigv4.CognitoUserPoolsAuthProvider;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import javax.annotation.Nonnull;

import type.CreateTaskInput;

public class AddTaskActivity extends AppCompatActivity {

    private AWSAppSyncClient mAWSAppSyncClient;

    TextView t_name;
    TextView t_date;
    TextView t_time;
    TextView t_start;
    TextView t_end;
    TextView t_alarm;
    Button bt_task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        mAWSAppSyncClient = AWSAppSyncClient.builder()
                .context(getApplicationContext())
                .awsConfiguration(new AWSConfiguration(getApplicationContext()))
                .cognitoUserPoolsAuthProvider(new CognitoUserPoolsAuthProvider() {
                    @Override
                    public String getLatestAuthToken() {
                        try {
                            return AWSMobileClient.getInstance().getTokens().getIdToken().getTokenString();
                        } catch (Exception e){
                            Log.e("APPSYNC_ERROR", e.getLocalizedMessage());
                            return e.getLocalizedMessage();
                        }
                    }
                })
                .build();

       t_name = (TextView)findViewById(R.id.text_name);
        t_date = (TextView)findViewById(R.id.text_date);
        t_time = (TextView)findViewById(R.id.text_time);
        t_start = (TextView)findViewById(R.id.text_start);
        t_end = (TextView)findViewById(R.id.text_end);
        t_alarm = (TextView)findViewById(R.id.text_alarmtime);
        bt_task = (Button)findViewById(R.id.bt_task);

        bt_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runMutation();
                finish();
            }
        });


    }

    public void runMutation(){
        CreateTaskInput createTaskInput = CreateTaskInput.builder().
                taskName(t_name.getText().toString()).
                date(2020).
                build();

        mAWSAppSyncClient.mutate(CreateTaskMutation.builder().input(createTaskInput).build())
                .enqueue(mutationCallback);
    }

    private GraphQLCall.Callback<CreateTaskMutation.Data> mutationCallback = new GraphQLCall.Callback<CreateTaskMutation.Data>() {
        @Override
        public void onResponse(@Nonnull Response<CreateTaskMutation.Data> response) {
            Log.i("Results", "Added Task");
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e("Error", e.toString());
        }
    };
    public void runQuery(){
        mAWSAppSyncClient.query(ListTasksQuery.builder().build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(todosCallback);
    }

    private GraphQLCall.Callback<ListTasksQuery.Data> todosCallback = new GraphQLCall.Callback<ListTasksQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<ListTasksQuery.Data> response) {
            Log.i("Results", response.data().listTasks().items().toString());
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e("ERROR", e.toString());
        }
    };
    private AppSyncSubscriptionCall subscriptionWatcher;

    private void subscribe(){
        OnCreateTaskSubscription subscription = OnCreateTaskSubscription.builder().build();
        subscriptionWatcher = mAWSAppSyncClient.subscribe(subscription);
        subscriptionWatcher.execute(subCallback);
    }

    private AppSyncSubscriptionCall.Callback subCallback = new AppSyncSubscriptionCall.Callback() {
        @Override
        public void onResponse(@Nonnull Response response) {
            Log.i("Response", response.data().toString());
        }
        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e("Error", e.toString());
        }
        @Override
        public void onCompleted() {
            Log.i("Completed", "Subscription completed");
        }
    };
}
