package io.thedatapirates.cashplan.domains.settings

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import io.thedatapirates.cashplan.R
import io.thedatapirates.cashplan.domains.cashflow.CashFlowActivity
import io.thedatapirates.cashplan.domains.expense.ExpenseActivity
import io.thedatapirates.cashplan.domains.helpcenter.HelpCenterActivity
import io.thedatapirates.cashplan.domains.home.HomeActivity
import io.thedatapirates.cashplan.domains.investment.InvestmentActivity
import io.thedatapirates.cashplan.domains.login.LoginActivity
import io.thedatapirates.cashplan.domains.profile.ProfileActivity
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class SettingsActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle
    private val channel_id = "example_id_channel"
    private val notifID = 100

    /**
     * Handles adding listeners to switch between activities when any navigation button is selected
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        createNotificationChannel()

        bottomNav = findViewById(R.id.navSettingsBottomNavigation)
        navView = findViewById(R.id.nvSettingsTopNavigationWithHeader)
        drawerLayout = findViewById(R.id.dlSettingsActivity)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sharedPreferences = this.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)

        val headerView = navView.getHeaderView(0)
        val customerNameView = headerView.findViewById<TextView>(R.id.tvCustomerName)
        val customerEmailView = headerView.findViewById<TextView>(R.id.tvCustomerEmail)
        val customerName = "${
            sharedPreferences.getString(
                "customerFirstName",
                ""
            )
        } ${sharedPreferences.getString("customerLastName", "")}"
        customerNameView.text = customerName
        customerEmailView.text = sharedPreferences.getString("userEmail", "")

        navView.setNavigationItemSelectedListener {

            it.isChecked = true

            when (it.itemId) {
                R.id.navProfileActivity -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    overridePendingTransition(0, 0)
                }
                R.id.navLogOut -> {
                    val editSettings =
                        this.getSharedPreferences("UserInfo", Context.MODE_PRIVATE).edit()

                    // removes all settings related to getting customer information in api
                    editSettings.remove("accessToken")
                    editSettings.remove("refresherToken")
                    editSettings.remove("userEmail")
                    editSettings.apply()

                    startActivity(Intent(this, LoginActivity::class.java))
                    overridePendingTransition(0, 0)
                }
                R.id.navHelpCenterActivity -> {
                    startActivity(Intent(this, HelpCenterActivity::class.java))
                    overridePendingTransition(0, 0)
                }
                R.id.navShare -> navView.menu.findItem(R.id.navShare).isChecked = false
                R.id.navWriteReview -> navView.menu.findItem(R.id.navWriteReview).isChecked = false
                R.id.navFacebook -> navView.menu.findItem(R.id.navFacebook).isChecked = false
                R.id.navInstagram -> navView.menu.findItem(R.id.navInstagram).isChecked = false
                R.id.navSnapchat -> navView.menu.findItem(R.id.navSnapchat).isChecked = false
                R.id.navLinkedIn -> navView.menu.findItem(R.id.navLinkedIn).isChecked = false
            }
            true
        }

        navSettingsBottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navHomeActivity -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    overridePendingTransition(0, 0)
                }
                R.id.navCashFlowActivity -> {
                    startActivity(Intent(this, CashFlowActivity::class.java))
                    overridePendingTransition(0, 0)
                }
                R.id.navExpenseActivity -> {
                    startActivity(Intent(this, ExpenseActivity::class.java))
                    overridePendingTransition(0, 0)
                }
                R.id.navInvestmentActivity -> {
                    startActivity(Intent(this, InvestmentActivity::class.java))
                    overridePendingTransition(0, 0)
                }
            }
            true
        }

        bottomNav.selectedItemId = R.id.navInvisible
        navView.setCheckedItem(R.id.navSettingsActivity)

        object : CountDownTimer(10000,1000){
            override fun onTick(tick: Long) {
                tick - 1000;
            }
            override fun onFinish() {
                sendNotification()
            }
        }.start()

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    private fun createNotificationChannel() {
        // check if device is running android 8.0 or not
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //creates notification item in the device settings
            val name = "All Notifications"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channel_id, name, importance).apply {
                enableVibration(true)
                description = descriptionText
            }
            // Register the channel with the users device
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

   fun sendNotification(){
       val intent = Intent(this, SettingsActivity::class.java).apply {
           flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
       }
       val pendingIntent : PendingIntent = getActivity(this, 0 ,intent, PendingIntent.FLAG_IMMUTABLE)
       val bitmap = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.ic_launcher_background)
       val bitmapLarge = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.ic_launcher_foreground)

       //the actual notification itself
        val builder = NotificationCompat.Builder(this, channel_id)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Notification Title")
            .setContentText("Notification Content")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

//            .setLargeIcon(bitmapLarge)
//            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
//            .setAutoCancel(true)
//            .setStyle((NotificationCompat.BigTextStyle().bigText("the text that takes numerous lines")))
        with(NotificationManagerCompat.from(this)){
            notify(notifID, builder.build())
        }
    }
}