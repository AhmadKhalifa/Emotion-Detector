package fci.machinelearning.emotiondetector.ui.activity

import android.os.Bundle
import fci.machinelearning.emotiondetector.R
import fci.machinelearning.emotiondetector.ui.base.BaseActivity
import fci.machinelearning.emotiondetector.ui.fragement.CameraFragment
import fci.machinelearning.emotiondetector.viewmodel.activity.CameraActivityViewModel
import kotlinx.android.synthetic.main.activity_camera.*

class CameraActivity :
    BaseActivity<CameraActivityViewModel>(),
    CameraFragment.OnFragmentInteractionListener {

    private var _fragment: CameraFragment? = null
    private val fragment: CameraFragment
        get() {
            if (_fragment == null) {
                val existingFragment = supportFragmentManager.findFragmentByTag(CameraFragment.TAG)
                if (existingFragment != null) {
                    _fragment = existingFragment as CameraFragment
                }
            }
            _fragment = _fragment ?: CameraFragment.newInstance()
            return _fragment as CameraFragment
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        supportFragmentManager.run fragmentManager@{
            findFragmentByTag(CameraFragment.TAG) ?: run fragment@{
                beginTransaction().add(fragmentPlaceHolderLayout.id, fragment, CameraFragment.TAG).commit()
            }
        }
    }

    override fun getViewModelInstance() = CameraActivityViewModel.getInstance(this)
}
