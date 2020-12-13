package com.thawdezin.fludroid.ui.slideshow

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.thawdezin.fludroid.R
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.view.FlutterMain

class SlideshowFragment : Fragment() {

    private lateinit var slideshowViewModel: SlideshowViewModel
    private var flutterView: FlutterView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)
        //val textView: FlutterView = root.findViewById(R.id.text_slideshow)
        return inflater.inflate(R.layout.fragment_slideshow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val args = getArgsFromIntent(intent)
        // check if flutterEngine is null
        if (flutterEngine == null) {
            //println(args)
            flutterEngine = FlutterEngine(requireContext())//, "args")
            flutterEngine!!.dartExecutor.executeDartEntrypoint(
                // set which of dart methode will be used here
                DartExecutor.DartEntrypoint(FlutterMain.findAppBundlePath(), "main")
                // to set here the main methode you can use this function to do this
                // inteade of DartEntrypoint(FlutterMain.findAppBundlePath(),"myMainDartMethod")
                // write this mdethode DartEntrypoint.createDefault()
            )
        }
        //setContentView(R.layout.flutter_view_layout) // <- the layout that i use to show the flutterActivity inside it
        flutterView = view.findViewById(R.id.flutter_view)
        flutterView?.attachToFlutterEngine(flutterEngine!!)
    }

    private fun getArgsFromIntent(intent: Intent): Array<String>? {
        // Before adding more entries to this list, consider that arbitrary
        // Android applications can generate intents with extra data and that
        // there are many security-sensitive args in the binary.
        val args = ArrayList<String>()
        if (intent.getBooleanExtra("trace-startup", false)) {
            args.add("--trace-startup")
        }
        if (intent.getBooleanExtra("start-paused", false)) {
            args.add("--start-paused")
        }
        if (intent.getBooleanExtra("enable-dart-profiling", false)) {
            args.add("--enable-dart-profiling")
        }
        if (args.isNotEmpty()) {
            return args.toTypedArray()
        }
        return null
    }


    // hire will be tested if the channel lifecycle is resumed
    override fun onResume() {
        super.onResume()
        Log.e(TAG,"onResume")
        flutterEngine!!.lifecycleChannel.appIsResumed()
    }
    // hire will be tested if the channel lifecycle is paused
    override fun onPause() {
        super.onPause()
        Log.e(TAG,"onPause")
        flutterEngine!!.lifecycleChannel.appIsInactive()
    }
    // hire will be tested if the channel lifecycle is stoped
    override fun onStop() {
        super.onStop()
        Log.e(TAG,"onStop")
        flutterEngine!!.lifecycleChannel.appIsPaused()
    }
    // hire will be tested if the channel lifecycle is destroied
    override fun onDestroy() {
        flutterView!!.detachFromFlutterEngine()
        Log.e(TAG,"onDestroy")
        super.onDestroy()
    }
    companion object {
        //
        private var flutterEngine: FlutterEngine? = null
        private var TAG: String = "Flutter Widget"
    }
}


