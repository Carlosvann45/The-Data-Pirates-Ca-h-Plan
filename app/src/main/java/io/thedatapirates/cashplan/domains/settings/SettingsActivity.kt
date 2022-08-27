package io.thedatapirates.cashplan.domains.settings

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
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

    /**
     * Handles adding listeners to switch between activities when any navigation button is selected
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

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
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    var builder = NotificationCompat.Builder(this, "Channel_ID")
        .setSmallIcon(R.drawable.ic_launcher_background)
        .setContentTitle("Notification Title")
        .setContentText("Notification Content")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.Template)
            val descriptionText = getString(R.string.BTitle_Notifications)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("Channel_ID2", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}