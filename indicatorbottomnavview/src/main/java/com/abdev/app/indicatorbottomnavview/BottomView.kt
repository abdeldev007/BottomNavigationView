package com.abdev.app.indicatorbottomnavview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import kotlin.math.abs
import kotlin.math.pow


open class AbdevBottomNavigationView : LinearLayout {
    private var initialMenuItemPositionY: Float = 0f
    private var lastMenuItemIndex = R.id.menuItem1
    val STIFFNESS = SpringForce.STIFFNESS_LOW
    val DAMPING_MEDIUM_RATIO = 0.4f
    val DAMPING_LOW_RATIO = 0.6f

    object AbdevBottomNavigationViewConstants {
        //region Constant
        const val WEEKS_COUNT = 10
        //endregion
    }


    private var indicatorColor: Int? = ContextCompat.getColor(context, R.color.primaryColor)
    private var listener: ItemsClickListener? = null
    private var indicatorWidth: Float? = 5f
    private var textColor: Int? = ContextCompat.getColor(context, R.color.black)

    private var navigationViewBackground: Int? = ContextCompat.getColor(context, R.color.white)


    constructor(context: Context) : super(context) {
        initControl(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initControl(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initControl(context, attrs)
    }

    fun setListener(listener: ItemsClickListener) {
        this.listener = listener
    }

    private fun initControl(context: Context, attrs: AttributeSet?) {
        val attributeArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.AbdevBottomNavigationView
        )

        indicatorColor = attributeArray.getResourceId(
            R.styleable.AbdevBottomNavigationView_abd_indicator_color,
            R.color.primaryColor
        )
        indicatorWidth = attributeArray.getDimension(
            R.styleable.AbdevBottomNavigationView_abd_indicator_width,
            5f
        )
        textColor = attributeArray.getResourceId(
            R.styleable.AbdevBottomNavigationView_abd_text_color,
            R.color.black
        )

        navigationViewBackground =
            attributeArray.getResourceId(
                R.styleable.AbdevBottomNavigationView_abd_background_color,
                R.color.white
            )
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.bottom_navigation_bar, this)
        assignUiElements()

        attributeArray.recycle()
    }

    lateinit var menuItem1Text: TextView
    lateinit var menuItem2Text: TextView
    lateinit var menuItem3Text: TextView
    lateinit var menuItem4Text: TextView

    lateinit var menuItem1: FrameLayout
    lateinit var menuItem2: FrameLayout
    lateinit var menuItem3: FrameLayout
    lateinit var menuItem4: FrameLayout

    lateinit var menuItem1Icon: ImageView
    lateinit var menuItem2Icon: ImageView
    lateinit var menuItem3Icon: ImageView
    lateinit var menuItem4Icon: ImageView
    lateinit var indicatorCardView: View
    lateinit var navView: FrameLayout

    open fun assignUiElements() {
        navView = findViewById<FrameLayout>(R.id.nav_view)


        menuItem1Text = findViewById<TextView>(R.id.menuItem1Text)
        menuItem2Text = findViewById<TextView>(R.id.menuItem2Text)
        menuItem3Text = findViewById<TextView>(R.id.menuItem3Text)
        menuItem4Text = findViewById<TextView>(R.id.menuItem4Text)

        menuItem1 = findViewById<FrameLayout>(R.id.menuItem1)
        menuItem2 = findViewById<FrameLayout>(R.id.menuItem2)
        menuItem3 = findViewById<FrameLayout>(R.id.menuItem3)
        menuItem4 = findViewById<FrameLayout>(R.id.menuItem4)

        menuItem1Icon = findViewById<ImageView>(R.id.menuItem1Icon)
        menuItem2Icon = findViewById<ImageView>(R.id.menuItem2Icon)
        menuItem3Icon = findViewById<ImageView>(R.id.menuItem3Icon)
        menuItem4Icon = findViewById<ImageView>(R.id.menuItem4Icon)

        indicatorCardView = findViewById<View>(R.id.indicatorCardView)

        navView.setBackgroundColor(navigationViewBackground!!)
        menuItem1Text.setTextColor(textColor!!)
        menuItem2Text.setTextColor(textColor!!)
        menuItem3Text.setTextColor(textColor!!)
        menuItem4Text.setTextColor(textColor!!)


        menuItem1Icon.setImageResource(R.drawable.ic_baseline_home_24)
        menuItem2Icon.setImageResource(textColor!!)
        menuItem3Icon.setImageResource(textColor!!)
        menuItem4Icon.setImageResource(textColor!!)
        navView.background = ContextCompat.getDrawable(context,navigationViewBackground!!)

        indicatorCardView.backgroundTintList = ContextCompat.getColorStateList(context,indicatorColor!!)

        menuItem1.setOnClickListener {

            moveIndicator(indicatorCardView, menuItem1, R.id.menuItem1)


        }
        menuItem2.setOnClickListener {

            moveIndicator(indicatorCardView, menuItem2, R.id.menuItem2)


        }
        menuItem3.setOnClickListener {

            moveIndicator(indicatorCardView, menuItem3, R.id.menuItem3)


        }
        menuItem4.setOnClickListener {

            moveIndicator(indicatorCardView, menuItem4, R.id.menuItem4)


        }


    }

    private fun moveIndicator(
        indicatorCardView: View,
        targetView: FrameLayout,
        clickedMenuItemId: Int
    ) {
        disableMenuItemFastClicks()


        val springAnimation = createSpringAnimation(
            indicatorCardView,
            SpringAnimation.X,
            "x",
            indicatorCardView.x,
            targetView.x + targetView.width / 2 - indicatorCardView.width / 2,
            STIFFNESS,
            if (abs(lastMenuItemIndex - clickedMenuItemId) == 1) DAMPING_MEDIUM_RATIO else DAMPING_LOW_RATIO
        )

        springAnimation.addEndListener { animation, canceled, value, velocity ->


        }
        springAnimation.start()
        Log.e("infos", "$lastMenuItemIndex:$clickedMenuItemId")

        val animatorSet = createTextAndIconFadeAnimation(clickedMenuItemId)

        animatorSet.start()
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {

                when (clickedMenuItemId) {
                    R.id.menuItem1 -> {
                        listener?.onItemClicked(menuItem1, R.id.menuItem1)

                    }
                    R.id.menuItem2 -> {

                        listener?.onItemClicked(menuItem2, R.id.menuItem2)

                    }
                    R.id.menuItem3 -> {
                        listener?.onItemClicked(menuItem3, R.id.menuItem3)

                    }
                    R.id.menuItem4 -> {
                        listener?.onItemClicked(menuItem4, R.id.menuItem4)

                    }
                }

            }
        })
        lastMenuItemIndex = clickedMenuItemId

//               animatorMoveSet.start()
//            }
//        })

    }

    private fun createTextAndIconFadeAnimation(clickedMenuItemId: Int): AnimatorSet {
        val animatorSet = AnimatorSet()
        when (lastMenuItemIndex) {
            R.id.menuItem1 -> {
                when (clickedMenuItemId) {
                    R.id.menuItem1 -> {

                    }
                    R.id.menuItem2 -> {
                        //  fadeAnimation(true, menuItem2Text)
                        animatorSet.playTogether(
                            moveUpMenuItemText(menuItem1Text),
                            moveDownMenuItemText(menuItem2Text, menuItem2Icon),
                        )
                        fadeAnimation(false, menuItem2Icon)
                        fadeAnimation(true, menuItem1Icon)
                    }
                    R.id.menuItem3 -> {
                        //      fadeAnimation(true, menuItem3Text)
                        animatorSet.playTogether(
                            moveUpMenuItemText(menuItem1Text),
                            moveDownMenuItemText(menuItem3Text, menuItem3Icon),
                        )
                        fadeAnimation(false, menuItem3Icon)
                        fadeAnimation(true, menuItem1Icon)
                    }
                    R.id.menuItem4 -> {
                        //     fadeAnimation(true, menuItem4Text)
                        animatorSet.playTogether(
                            moveUpMenuItemText(menuItem1Text),
                            moveDownMenuItemText(menuItem4Text, menuItem4Icon),
                        )
                        fadeAnimation(false, menuItem4Icon)
                        fadeAnimation(true, menuItem1Icon)
                    }
                }

            }
            R.id.menuItem2 -> {
                when (clickedMenuItemId) {
                    R.id.menuItem1 -> {
                        //    fadeAnimation(true, menuItem1Text)
                        animatorSet.playTogether(
                            moveUpMenuItemText(menuItem2Text),
                            moveDownMenuItemText(menuItem1Text, menuItem1Icon),
                        )
                        fadeAnimation(false, menuItem1Icon)
                        fadeAnimation(true, menuItem2Icon)
                    }
                    R.id.menuItem2 -> {

                    }
                    R.id.menuItem3 -> {
                        //    fadeAnimation(true, menuItem3Text)
                        animatorSet.playTogether(

                            moveUpMenuItemText(menuItem2Text),
                            moveDownMenuItemText(menuItem3Text, menuItem3Icon),
                        )
                        fadeAnimation(false, menuItem3Icon)
                        fadeAnimation(true, menuItem2Icon)
                    }
                    R.id.menuItem4 -> {
                        //   fadeAnimation(true, menuItem4Text)
                        animatorSet.playTogether(

                            moveUpMenuItemText(menuItem2Text),
                            moveDownMenuItemText(menuItem4Text, menuItem4Icon),
                        )
                        fadeAnimation(false, menuItem4Icon)
                        fadeAnimation(true, menuItem2Icon)
                    }
                }
            }
            R.id.menuItem3 -> {
                when (clickedMenuItemId) {
                    R.id.menuItem1 -> {
                        //   fadeAnimation(true, menuItem1Text)
                        animatorSet.playTogether(

                            moveUpMenuItemText(menuItem3Text),
                            moveDownMenuItemText(menuItem1Text, menuItem1Icon),
                        )
                        fadeAnimation(false, menuItem1Icon)
                        fadeAnimation(true, menuItem3Icon)
                    }
                    R.id.menuItem2 -> {
                        //  fadeAnimation(true, menuItem2Text)
                        animatorSet.playTogether(

                            moveUpMenuItemText(menuItem3Text),
                            moveDownMenuItemText(menuItem2Text, menuItem2Icon),
                        )
                        fadeAnimation(false, menuItem2Icon)
                        fadeAnimation(true, menuItem3Icon)
                    }
                    R.id.menuItem3 -> {

                    }
                    R.id.menuItem4 -> {
                        //  fadeAnimation(true, menuItem4Text)
                        animatorSet.playTogether(
                            moveUpMenuItemText(menuItem3Text),
                            moveDownMenuItemText(menuItem4Text, menuItem4Icon),
                        )
                        fadeAnimation(false, menuItem4Icon)
                        fadeAnimation(true, menuItem3Icon)
                    }
                }
            }
            R.id.menuItem4 -> {
                when (clickedMenuItemId) {
                    R.id.menuItem1 -> {
                        //   fadeAnimation(true, menuItem3Text)
                        animatorSet.playTogether(
                            moveUpMenuItemText(menuItem4Text),
                            moveDownMenuItemText(menuItem1Text, menuItem1Icon),
                        )
                        fadeAnimation(false, menuItem1Icon)
                        fadeAnimation(true, menuItem4Icon)
                    }
                    R.id.menuItem2 -> {
                        //   fadeAnimation(true, menuItem2Text)
                        animatorSet.playTogether(
                            moveUpMenuItemText(menuItem4Text),
                            moveDownMenuItemText(menuItem2Text, menuItem2Icon),
                        )

                        fadeAnimation(false, menuItem2Icon)

                        fadeAnimation(true, menuItem4Icon)
                    }
                    R.id.menuItem3 -> {
                        //   fadeAnimation(true, menuItem3Text)
                        animatorSet.playTogether(
                            moveUpMenuItemText(menuItem4Text),
                            moveDownMenuItemText(menuItem3Text, menuItem3Icon),
                        )
                        fadeAnimation(false, menuItem3Icon)
                        fadeAnimation(true, menuItem4Icon)
                    }
                    R.id.menuItem4 -> {

                    }
                }
            }
        }
        return animatorSet
    }

    private fun disableMenuItemFastClicks() {
        menuItem1.isEnabled = false
        menuItem2.isEnabled = false
        menuItem3.isEnabled = false
        menuItem4.isEnabled = false
        val handler = Handler(Looper.getMainLooper() /*UI thread*/)

        var workRunnable: Runnable = object : Runnable {
            override fun run() {

                handler.postDelayed(this, 500)
            }
        }
        workRunnable = Runnable {
            menuItem1.isEnabled = true
            menuItem2.isEnabled = true
            menuItem3.isEnabled = true
            menuItem4.isEnabled = true

        }
        handler.postDelayed(workRunnable, 200 /*delay*/)

    }

    private fun moveDownMenuItemText(animatedView: View, targetView: View): AnimatorSet {

        val dest = IntArray(2)
        targetView.getLocationOnScreen(dest)


        val y: Float = 0f
        Log.e("views_data", "${dest[0]}  :  ${dest[1]}  x= $y")

        val translatorY: Animator = ObjectAnimator.ofFloat(
            animatedView,
            View.Y,
            y,
            70f
        )

        translatorY.interpolator = TimeInterpolator { input: Float ->
            (-(input - 1).toDouble().pow(2.0) + 1f).toFloat()
        }

        val fadeIn = ObjectAnimator.ofFloat(animatedView, "alpha", 0f, 1f)
        fadeIn.duration = 300
        val animatorMoveSet = AnimatorSet()
        animatorMoveSet.duration = 500
        animatorMoveSet.playTogether(translatorY, fadeIn)
        animatorMoveSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                animatedView.requestLayout()

            }
        })
        return animatorMoveSet
    }

    private fun moveUpMenuItemText(animatedView: View): AnimatorSet {


        val translatorY: Animator = ObjectAnimator.ofFloat(
            animatedView,
            View.Y,
            animatedView.y,
            -10f
        )

        translatorY.interpolator = TimeInterpolator { input: Float ->
            (-(input - 1).toDouble().pow(2.0) + 1f).toFloat()
        }

        val fadeOut = ObjectAnimator.ofFloat(animatedView, "alpha", 1f, 0f)


        fadeOut.duration = 100
        val animatorMoveSet = AnimatorSet()
        animatorMoveSet.duration = 300
        animatorMoveSet.playTogether(translatorY, fadeOut)
        animatorMoveSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                animatedView.requestLayout()
            }
        })
        return animatorMoveSet
    }

    private fun fadeAnimation(isFadeIn: Boolean, myView: View) {
        val mAnimationSet = AnimatorSet()
        if (isFadeIn) {
            val fadeIn = ObjectAnimator.ofFloat(myView, "alpha", 0f, 1f)
            fadeIn.duration = 400
            mAnimationSet.play(fadeIn)
            mAnimationSet.start()
        } else {
            val fadeOut = ObjectAnimator.ofFloat(myView, "alpha", 1f, 0f)
            fadeOut.duration = 200
            mAnimationSet.play(fadeOut)
            mAnimationSet.start()
        }

    }

    private fun createSpringAnimation(
        view: View, property: DynamicAnimation.ViewProperty,
        xy: String, startPosition: Float, finalPosition: Float,
        stiffness: Float, dampingRatio: Float
    ): SpringAnimation {
        if (xy == "x") {
            view.animate().x(startPosition).setDuration(0).start()
        } else {
            view.animate().y(startPosition).setDuration(0).start()
        }

        val animation = SpringAnimation(view, property)
        val spring = SpringForce(finalPosition)
        spring.stiffness = stiffness
        spring.dampingRatio = dampingRatio
        animation.spring = spring
        return animation
    }

    fun setMenuItemsText(s: String, s1: String, s2: String, s3: String) {
        menuItem1Text.text = s
        menuItem2Text.text = s1
        menuItem3Text.text = s2
        menuItem4Text.text = s3
    }

    fun setMenuIcons(
        icon1: Int,
        icon2: Int,
        icon3: Int,
        icon4: Int
    ) {
        menuItem1Icon.setImageResource(icon1)
        menuItem2Icon.setImageResource(icon2)
        menuItem3Icon.setImageResource(icon3)
        menuItem4Icon.setImageResource(icon4)

    }
}
