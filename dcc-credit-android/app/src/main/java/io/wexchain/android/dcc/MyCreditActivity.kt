package io.wexchain.android.dcc

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.Toolbar
import io.wexchain.android.dcc.base.BindActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.wexchain.android.common.navigateTo
import io.wexchain.android.common.setWindowExtended
import io.wexchain.android.dcc.chain.CertOperations
import io.wexchain.android.dcc.chain.PassportOperations
import io.wexchain.android.dcc.domain.CertificationType
import io.wexchain.android.dcc.vm.AuthenticationStatusVm
import io.wexchain.android.dcc.vm.domain.UserCertStatus
import io.wexchain.dcc.R
import io.wexchain.dcc.databinding.ActivityMyCreditBinding

class MyCreditActivity : BindActivity<ActivityMyCreditBinding>() {
    override val contentLayoutId: Int = R.layout.activity_my_credit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWindowExtended()
        initToolbarS()
        binding.asIdVm = obtainAuthStatus(CertificationType.ID)
        binding.asBankVm = obtainAuthStatus(CertificationType.BANK)
        binding.asMobileVm = obtainAuthStatus(CertificationType.MOBILE)
        binding.asPersonalVm = obtainAuthStatus(CertificationType.PERSONAL)
    }

    private fun initToolbarS(showHomeAsUp :Boolean = true): Toolbar? {
        toolbar = findViewById(R.id.toolbar)
        val tb = toolbar
        if (tb != null) {
            setSupportActionBar(toolbar)
            toolbarTitle = findViewById(R.id.toolbar_title)
            intendedTitle?.let { title = it }
        }
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(showHomeAsUp)
            setDisplayShowHomeEnabled(showHomeAsUp)
            if (toolbarTitle != null) {
                setDisplayShowTitleEnabled(false)
            }
        }
        return toolbar
    }

    private fun obtainAuthStatus(certificationType: CertificationType): AuthenticationStatusVm? {
        return ViewModelProviders.of(this)[certificationType.name, AuthenticationStatusVm::class.java]
                .apply {
                    //todo 
                    title.set(getCertTypeTitle(certificationType))
                    authDetail.set(getDescription(certificationType))
                    this.status.set(CertOperations.getCertStatus(certificationType))
                    this.certificationType.set(certificationType)
                    this.performOperationEvent.observe(this@MyCreditActivity, Observer {
                        it?.let {
                            val type = it.first
                            val certStatus = it.second
                            if (type != null && certStatus != null) {
                                this@MyCreditActivity.performOperation(type, certStatus)
                            }
                        }
                    })
                }
    }

    override fun onResume() {
        super.onResume()
        refreshCertStatus()
    }

    private fun refreshCertStatus() {
        listOf(binding.asIdVm, binding.asBankVm, binding.asMobileVm, binding.asPersonalVm).forEach {
            it?.let { vm ->
                vm.certificationType.get()?.let {
                    vm.status.set(CertOperations.getCertStatus(it))
                }
            }
        }
        binding.asMobileVm?.let {
            if (it.status.get() == UserCertStatus.INCOMPLETE) {
                //get report
                val passport = App.get().passportRepository.getCurrentPassport()!!
                CertOperations.getCommunicationLogReport(passport)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ data ->
                            if (data.fail) {
                                // generate report fail
                                CertOperations.onCmLogFail()
                                refreshCertStatus()
                            } else {
                                val reportData = data.reportData
                                if (data.hasCompleted() && reportData != null) {
                                    CertOperations.onCmLogSuccessGot(reportData)
                                    refreshCertStatus()
                                }
                            }
                        })
            }
        }
    }

    private fun getDescription(certificationType: CertificationType): String {
        return when (certificationType) {
            CertificationType.ID -> "真实身份认证"
            CertificationType.PERSONAL -> "更安全的评估"
            CertificationType.BANK -> "提高审核的通过率"
            CertificationType.MOBILE -> "提高审核的通过率和借款额度"
        }
    }

    private fun getCertTypeTitle(certificationType: CertificationType): String {
        return when (certificationType) {
            CertificationType.ID -> "身份证认证"
            CertificationType.PERSONAL -> "真实信息认证"
            CertificationType.BANK -> "银行卡认证"
            CertificationType.MOBILE -> "运营商认证"
        }
    }

    private fun performOperation(certificationType: CertificationType, status: UserCertStatus) {
        when (certificationType) {
            CertificationType.ID -> {
                if (status == UserCertStatus.DONE) {
                    navigateTo(IdCertificationActivity::class.java)
                } else {
                    PassportOperations.ensureCaValidity(this) {
                        navigateTo(SubmitIdActivity::class.java)
                    }
                }
            }
            CertificationType.PERSONAL -> {
//                navigateTo(SubmitPersonalInfoActivity::class.java)
            }
            CertificationType.BANK ->
                if (status == UserCertStatus.DONE) {
                    navigateTo(BankCardCertificationActivity::class.java)
                } else {
                    PassportOperations.ensureCaValidity(this) {
                        navigateTo(SubmitBankCardActivity::class.java)
                    }
                }
            CertificationType.MOBILE -> {
                when (status) {
                    UserCertStatus.DONE -> {
                        navigateTo(CmLogCertificationActivity::class.java)
                    }
                    UserCertStatus.NONE -> {
                        PassportOperations.ensureCaValidity(this) {
                            navigateTo(SubmitCommunicationLogActivity::class.java)
                        }
                    }
                    UserCertStatus.INCOMPLETE -> {
                        // get report processing
                    }
                }
            }
        }
    }


}
