package com.test.currencyconverter

import android.content.Intent
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

var userID:String? = ""
var user_name:String? = ""

open class NavigationPane: AppCompatActivity() {

    private lateinit var mToggle:ActionBarDrawerToggle

    fun onCreateDrawer(mDrawerLayout: DrawerLayout) {

        val navigationView:NavigationView = findViewById(R.id.nav_view)
        val headerView = navigationView.getHeaderView(0)
        val emailId: TextView = headerView.findViewById(R.id.email_ID)
        val nameId: TextView = headerView.findViewById(R.id.name_ID)

        val mAuth = FirebaseAuth.getInstance()
        val userRef = FirebaseDatabase.getInstance().reference.child("Users")

        userID =  mAuth.currentUser!!.email
        emailId.text = userID

        userRef.addValueEventListener((object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                println(error.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                val firstName = p0.child(mAuth.currentUser!!.uid).child("firstName").value
                val lastName = p0.child(mAuth.currentUser!!.uid).child("lastName").value
                user_name = "$firstName $lastName"
                nameId.text = user_name
            }
        }))

        mToggle = ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close)
        mDrawerLayout.addDrawerListener(mToggle)
        mToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            mDrawerLayout.closeDrawers()

            when (menuItem.itemId) {
                R.id.home -> {
                    val homeIntent = Intent(this, HomePage::class.java)
                    startActivity(homeIntent)
                }
                R.id.market_status -> {
                    val marketStatusIntent = Intent(this, MarketStatusPage::class.java)
                    startActivity(marketStatusIntent)
                }
                R.id.help -> {
                    val helpIntent = Intent(this, HelpPage::class.java)
                    startActivity(helpIntent)
                }
                R.id.contact -> {
                    Toast.makeText(this, "Visit: www.loonycorn.com", Toast.LENGTH_LONG).show()
                }
                R.id.logout -> {
                    FirebaseAuth.getInstance().signOut()
                    val loginIntent = Intent(this, LoginPage::class.java)
                    startActivity(loginIntent)
                }
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (mToggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}