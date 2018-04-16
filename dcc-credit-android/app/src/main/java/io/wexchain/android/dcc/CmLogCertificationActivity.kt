package io.wexchain.android.dcc

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.wexmarket.android.passport.base.BindActivity
import io.wexchain.android.common.getViewModel
import io.wexchain.android.common.navigateTo
import io.wexchain.android.dcc.chain.CertOperations
import io.wexchain.android.dcc.vm.CertificationDataVm
import io.wexchain.android.dcc.vm.ViewModelHelper
import io.wexchain.auth.R
import io.wexchain.auth.databinding.ActivityCertificationDataBinding

class CmLogCertificationActivity : BindActivity<ActivityCertificationDataBinding>() {
    override val contentLayoutId: Int = R.layout.activity_certification_data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar()
        val data = CertOperations.getCertIdData()!!
        val vm = getViewModel<CertificationDataVm>().apply {
            this.title1.set(getString(R.string.phone_num))
            this.title2.set(getString(R.string.phone_owner_name))
            this.title3.set(getString(R.string.cert_expired_label))
            this.value1.set(data.name)
            this.value2.set(data.id)
            this.value3.set(ViewModelHelper.expiredText(data.expired))
        }
        vm.renewEvent.observe(this, Observer {
            navigateTo(SubmitIdActivity::class.java)
        })
        binding.vm = vm
    }
}