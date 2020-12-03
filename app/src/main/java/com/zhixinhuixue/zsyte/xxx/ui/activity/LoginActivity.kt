package com.zhixinhuixue.zsyte.xxx.ui.activity

import android.os.Bundle
import android.widget.CompoundButton
import androidx.lifecycle.Observer
import com.zhixinhuixue.library.common.base.BaseDbActivity
import com.zhixinhuixue.library.common.ext.getStringExt
import com.zhixinhuixue.library.common.ext.initBack
import com.zhixinhuixue.library.common.ext.logD
import com.zhixinhuixue.library.common.ext.showDialogMessage
import com.zhixinhuixue.library.net.RequestMatchCode
import com.zhixinhuixue.library.net.entity.base.LoadStatusEntity
import com.zhixinhuixue.library.net.error.msg
import com.zhixinhuixue.zsyte.xxx.R
import com.zhixinhuixue.zsyte.xxx.databinding.ActivityLoginBinding
import com.zhixinhuixue.zsyte.xxx.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*

/**
 * 作者　: hegaojian
 * 时间　: 2020/11/18
 * 描述　: 虽然在Activity代码少了，但是DataBinding 不太好用
 */
class LoginActivity: BaseDbActivity<LoginViewModel,ActivityLoginBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        //初始化toolbar
        mToolbar.initBack(getStringExt(R.string.login_submit)) {
            finish()
        }
        mDataBind.viewModel = mViewModel
        mDataBind.click = LoginClickProxy()
    }

    /**
     * 请求成功
     */
    override fun onRequestSuccess() {
        //监听登录结果
        mViewModel.loginData.observe(this, Observer {
            //做保存信息等操作
            finish()
        })
    }

    /**
     * 请求失败
     * @param loadStatus LoadStatusEntity
     */
    override fun onRequestError(loadStatus: LoadStatusEntity) {
        when(loadStatus.matchCode){
            RequestMatchCode.LOGIN ->{
                showDialogMessage(loadStatus.errorMessage)
            }
        }
    }

    inner class LoginClickProxy{

        fun clear() {
            mViewModel.userName.set("")
        }
        //登录
        fun login(){
            when {
                mViewModel.userName.get().isEmpty() -> showDialogMessage("手机号不能为空")
                mViewModel.password.get().isEmpty() -> showDialogMessage("密码不能为空")
                else -> mViewModel.login(mViewModel.userName.get(), mViewModel.password.get())
            }
        }

        var onCheckedChangeListener =
            CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                mViewModel.isShowPwd.set(isChecked)
            }

    }

}