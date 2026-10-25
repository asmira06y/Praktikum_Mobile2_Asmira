
package id.ac.polbeng.asmira.workmanagerexample

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkQuery
import androidx.work.workDataOf
import id.ac.polbeng.asmira.workmanagerexample.databinding.ActivityMainBinding
import java.util.UUID
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val workManager by lazy {
        WorkManager.getInstance(applicationContext)
    }

    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresStorageNotLow(true)
        .setRequiresBatteryNotLow(true)
        .build()

    private val maxCounter = workDataOf(LoopWorker.COUNTER to 10)

    private lateinit var activityHomeBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityHomeBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityHomeBinding.root)

        activityHomeBinding.btnImageDownload.setOnClickListener {
            showLottieAnimation()

            activityHomeBinding.tvProgress.visibility = View.VISIBLE
            activityHomeBinding.downloadLayout.visibility = View.GONE

            createOneTimeWorkRequest()
            // createPeriodicWorkRequest()
            // createDelayedWorkRequest()
        }

        activityHomeBinding.btnQueryWork.setOnClickListener {
            queryWorkInfo()
        }
    }

    private fun showLottieAnimation() {
        activityHomeBinding.animationView.visibility = View.VISIBLE
    }

    private fun hideLottieAnimation() {
        activityHomeBinding.animationView.visibility = View.GONE
    }


    private fun createOneTimeWorkRequest() {
        val imageWorker = OneTimeWorkRequestBuilder<LoopWorker>()
            .setInputData(maxCounter)
            .addTag("imageWork")
            .build()

        workManager.enqueueUniqueWork(
            "oneTimeImageDownload",
            ExistingWorkPolicy.REPLACE,
            imageWorker
        )

        observeWork(imageWorker.id)
    }


    private fun observeWork(id: UUID) {
        workManager.getWorkInfoByIdLiveData(id)
            .observe(this) { info ->

                if (info != null) {
                    val progress = info.progress
                    val value = progress.getInt(
                        LoopWorker.PROGRESS,
                        0
                    )

                    activityHomeBinding.tvProgress.text =
                        "Progress $value%"

                    android.util.Log.d(
                        "WorkManagerStatus",
                        "ID: $id, State: ${info.state}, Progress: $value%"
                    )
                }

                if (info != null && info.state.isFinished) {
                    hideLottieAnimation()

                    activityHomeBinding.tvProgress.visibility = View.GONE
                    activityHomeBinding.btnImageDownload.visibility = View.GONE
                    activityHomeBinding.btnQueryWork.visibility = View.VISIBLE
                    activityHomeBinding.downloadLayout.visibility = View.VISIBLE

                    val message = info.outputData.getString(
                        LoopWorker.MESSAGE
                    )

                    if (message != null) {
                        Toast.makeText(
                            applicationContext,
                            message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }

    private fun createPeriodicWorkRequest() {
        val imageWorker = PeriodicWorkRequestBuilder<LoopWorker>(
            2,
            TimeUnit.MINUTES
        )
            .setInputData(maxCounter)
            .setConstraints(constraints)
            .addTag("imageWork")
            .build()

        workManager.enqueueUniquePeriodicWork(
            "periodicImageDownload",
            ExistingPeriodicWorkPolicy.KEEP,
            imageWorker
        )

        observeWork(imageWorker.id)
    }

    private fun createDelayedWorkRequest() {
        val imageWorker = OneTimeWorkRequestBuilder<LoopWorker>()
            .setInputData(maxCounter)
            .setConstraints(constraints)
            .setInitialDelay(30, TimeUnit.SECONDS)
            .addTag("imageWork")
            .build()

        workManager.enqueueUniqueWork(
            "delayedImageDownload",
            ExistingWorkPolicy.KEEP,
            imageWorker
        )

        observeWork(imageWorker.id)
    }

    private fun queryWorkInfo() {
        val workQuery = WorkQuery.Builder
            .fromTags(listOf("imageWork"))
            .addStates(listOf(WorkInfo.State.SUCCEEDED))
            .addUniqueWorkNames(
                listOf(
                    "oneTimeImageDownload",
                    "delayedImageDownload",
                    "periodicImageDownload"
                )
            )
            .build()

        workManager.getWorkInfosLiveData(workQuery)
            .observe(this) { workInfos ->

                activityHomeBinding.tvWorkInfo.visibility = View.VISIBLE

                activityHomeBinding.tvWorkInfo.text =
                    resources.getQuantityString(
                        R.plurals.text_work_desc,
                        workInfos.size,
                        workInfos.size
                    )
            }
    }
}
