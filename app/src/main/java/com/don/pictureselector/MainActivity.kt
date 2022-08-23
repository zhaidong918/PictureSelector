package com.don.pictureselector

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.don.pictureselector.databinding.ActivityMainBinding
import com.don.pictureselector.ui.PictureActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val launch = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == PictureFactory.RESULT_CODE) {
            it.data?.getParcelableArrayListExtra<PictureItem>(PictureFactory.RESULT_DATA)?.onEach {
                Log.d("MainActivity", it.path)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.fab.setOnAntiShakeClickListener { view ->
            launch.launch(Intent(this, PictureActivity::class.java))
        }




        /*Thread{
            ANE.jad_an("GD7cnhaTZPzOOP1LTPdSEyusqVvzx4b-DJh_ixTjEZIfb-k8gGiGdt462RwmghLnmFR0ZCDGNJSOi_GWzyvLhzG-1A30pjBgyEMnN5nsE57C4e8SCo0RL898lxb7YshTf5a4aYmmO-bN7OjZMzJ0GF-FqvIkiQMkUPa4NdrVuEaJ4fEzlDV0OMjDncDzBBcz-YzX7KnKYuT4miBXKU4k-yUT3WD6VpS4XyYAKwnVsp9X-TKtKaOumHhqNIGc0VNOVZ7M7UkbPz7OX5z_alOr-IrLrwLn0qspIIEo04r-Q1i5JwxCHm4ZVU_Ws-sSx8cCGJfwjucK7agfVvwvqrdofq_xDvciEvnWKOqSp6Z9bG3IYz_QYH9s5zgGUqxio6c2ZW4UpzncrgJ9vWeGC7d6dDNl3P6Mf91W53FAO-CRP3RXuAQzhHxwUJXjVJoHvqJxm839EuVTc8Z9Nt6aF8YdZLm_xJKp6znWZTq7Z6eQuDZ2QfCf6yRuTW0PI0DHBkMSI8zMPEUJXkGxLxvC3cxO9HHCcs5Mcrcs2HJRbg2AuvlYhLqiYiAYvoFjEKqaiQhfyZO3NgqEbjQZ7W2vO8TQL14EGw8yKDAtARAlHGQCfDBhPmLR1k_ZRMv2ZjjbLtNRfFM6DaKQ2XVP9lG4RmYeTuAZrLLvWU4jgoct_5HmMo5E6tGkREhEmhI_MUEi9BX3ChV1WZBTwQQlUSJxoNnLuzCmyHe4YtceH3e_66XPWM6UCfUZ1602OJ6IihPpagiGGLLTp4Y5RBOjTWpLiNpkpBwSeNWEhgtQ4euIPsbDa5H2MRJJmGPLJKI3aMloMWd9fANVeRqgpx5aNzU_BzJ2ICyfwLM-xWo6LtdrxrbB6Cuet5LhB5ufWp3VM89qjnlaKK75VHiHNjMPOWNLDL6SNjWcwijKiWWXy-Bu66aMlJKP6h3h91YIiXR-hlnzkXqGaZa0eGL6mf0U8fujZ5JCd5r3dbodazhyWjY6uKQbP0lQrtc1dItFLMoGVpUCVtiss0ftOmIf_X0ljEMgUaOvtzf_UYYa0aQIo8847adxBobtishnRLN3nDhcZKVRnCN8GbFY6lYzti0K_-4DKNgFWSR8zlkVlt2LxBJXZV3bpQxq5ytDRv8j3JdRV1D7_RINsXe1dXpdlXrlUfx3bQcEXGmo8QmBb2DYvyBYb7JLQG9OrBQ_PYGgLhfPo4fUBYEn0JWFmppWuGjxGXTArKn6WaUlurLaik4Fy9ETRBD4o1DbIjNlJGrpq9nig4ZrEEJ6Dlz2tu8QaAgnUHCf01X9GSYoRIvlZzLUd0POVIAHhI551ZIVkJHzrW1MYcX1rjXTgxxZtR4gnPVE2dLjwCyOzINZfAWiGt-Q_pqhbmVa2VMNDC24XwAp1ervVr1ypmrpyUZqgxzkXfh0ZqRX-EHWR4H-kvmaT5Qx816YbvOWt_jp3Ow1AG0VHDtbiIM5I_XWPOAP4HLYQQuSY7LCuWoWRg9bkMGk45ZXYK_pgijH1yFhARIeVq3J8I7Dk17i-iUWd71isXX6I5MrZ94xkwRlXvaqx4AoD8Rnq-fzXScuUJanyEws4cMdl8AZ61EqLrUuwEHWd8mfMctZSIDHlyR0PxSKesh1FZn34od8m4XX0NmMRoq_cqMQh6J3d49WpYLPUM5JxzJDKRfNBNtuyQHuES4YuTHhlR77O6yVukK6J6FFyZc_tPMpW5SlDlQaaZuNhKJOvyS2MRoRk44mMpfKa1lsDynWL8SUydpOb7gFlYXeFMbS_77EG4sRJB5DMIszqvLQym7zmhS0YF3auZ7dntJQ6JK-xxcJ0LaVYk3TvrwBGKcnPpqm1Sc89wo7a6x97jIqSgVYG3EljZoq15qW-kPMs4ZGCIauzG_ogwllNo6sz7q3irLUTVmXSuV6n2ylXKy3VQ10QcVHeNSo-2IPyMHBY5ZkbErQDizR6Hm_90Zi0PI25aT2MacWJhTn956gDLzaUMaIVBGWOVTVNVUJoyu1_lez609JPcwEDOvqw51W242bCkrFN153F3VRHVT_0zmvCEeAHCbo0hamsOyTgVi1oHh1JzQGkTim8sQWj_aeLS5yyS2PftYa1PxqEelghTDQxq6PmD9eEXmYX6dil8WoekdOfJ5O8h-P1j7xEfGKDSF2YAL5nUwnQiWj6cRhOUoXtQcCi1q-FKVz-mv4UN4uMQcaVfUsXdiO2q6wD2H4mgEUWuGigMic582cg1ZKB0aRbz6Q8bTSqq19UGhgqe3pA2E7dKE2MWe9hhEMSx01TJrr09W20wj9TnULegjuTxHcGP_lZX8DnY_ywzvAbc99JHAULPA8KS_xWOUD68Eohmk7lqQcWuOBCSY39DRPT-rlk3BU0wJE7fyoZijxTd3DzNiX23yku3bg3jFQvy_8g0Zv980idQSELJ9-haJVoeiTcAY-C1yhxmzycM9ZE5qvjCE0TVmmVH-i7GAUWooRbWzOlYbBm54Kin_GKF9PsQkkpl4Ng-t4IlqbFaAb-I8-T2fBKvefK_xigiW5nKz1egT-jbkzEVeaf9lVKmS8GeYiNEJPXFnJ9pJChVl4QLm1lOouEZclWyZJBbMrUZXS_sy6f7Gkxj7HVY4XR6Qac1fVY2uMgLMMjbDmDyIvWH81XZCVx6IsoRom-tA6rrOJZQCnxIbCIk37fC9eaXloM1omincoJR2mN_4waVQItC-27Q_OwjT5oMkqE2Uya2Uj7cL_RLupX63HD-tY9NUQw4T3lf55FdOwrYaazw5mq9y5kuVAP_kAZKB8k2j7qpsJXYLz4bpBiIBWFQoqdgFvV24uQxyqFPFEFbOQLy2-fLHh9uP4SbVIN85cVjQCoqAE-PKHW4m6CShTprLfBhtGC4-2gnKEiXDnYxKql2Dwiadp8gQeK7_sFyQsXk_VRR30ukOCx5TVBvPnpPNxTApUAma5mOCy4X87KUWMF-84xOOTHQfvhc98fCu0TVPVlEPzcnx88V-vAH089zeiemfOzLfUYpR2ADasSiqPOJrXpU52bmJY6oXx466qqzelT9DqTyvvQKR616lXcJMWGI7n84Z8BElyV__GsjQBlx13qK250-zGs-hHf3lomVnieq5WSXefWcTO4o6ad20PuA3jdlHFp8Ho6oP7rS9TX-6co2N_yNSq-P4c7yI4I8Sbe68ddGO9vuRb13C5bWsXNBI86vB-W3JhLVlaMZRlP2nySAn_4oZ9PQdpsrpQO6MQrCfYklSq4Fvvt8VRtE0f0qeC4uWmrKc4GgGYJnPpGt9amsR-qr8zArwgbIFYhddnmHEGsQ6I82_ZKLxE9u5oR3Pco2P7WIupS5n3PBna3NfcPiQ6gwxcvXo00N6q72A7xYud6M-yfJJBkKcoUTFtYZbcXs_jKrTmagjYcbCtFHePxKeA6XLEZkkplh8WPf1e5lY-xZvZoNWvT7_M2mCmNaxFHuLIz3YqJ7idt_0Dg0TNTudnKAJP-CDmJEzbgrUyp2SGnvwHcP4FYSF_15i1eFHZvR8wunmlZg6cPcK86RMXH2Oc3wTRdnbLJvOQMJowecDoZtuhY1Fe1KhEiQ8aDnl6Nc7i1_MB9t-RIyUfFdi8MV6SCLGm4JEpcfKN0k6EFd4rwtZQezpyt3GheBON3ZCI3INui__3Fpo2RYo8BqkWs2uZo_9Rob32k30-db8gsdRTqT1CoytlS5UgLz-OUNucqTl9cgqpq7-SrEjW94dw0w-z6ofEeYfOaUmlXF2YaBSwQVJR1hAW679cQTbSV_ECH6jtuxIT27auIel6GS3wiOV-TBvuZ27ccLno99WIs2zCoEm65vmbNwu2twNq9n4PXZTAp3XFhcBjOM7wl6LXxWGRQvh4LcnOyCKT6onVUFbCXevHim8sjdEffDH9Jssh-44oupILUodSEOUfbWkcVGdsUaErM7bgdaNVp8z-HjP3Cc5YuB8Ok69D_vtqYEd3mM1tUuY2KdmcnNQ-jm5ZzHFBuhehvC_uc4v9LdogKEjv3nX-fSLInv0qtrcvcwIzXYVi7PpvTgTvXmq99Y71rs0Op9MuL05LhUIdbBoc97o1Npblh6Avhd41ociTB0PYr85TBkKtk-rqF4c2VjwsRebk1-JnfcPNK-rsZIvsaSN22obhwKMJ9-mqCR-emrKAYatMJW8VTISezi6KC4g2g8qrcVRZrYyECJalhfu2BNeGzos9auuztlwFmPXcX6RdMJLcKC8GiQpF9XqVtx7cXZqrKGJzLdpBaRnVGz58SK2CKICyqrVJc6SvcT8a7wyLqOR5XkEpn5pKt_p25TJNviKtukA5KYNMhhadoI2opWEk7fvu2SOZXvS3oT8uNp0CmsScgiYrtZWL1L705MndHguaecqHh9WKOWRshFpmUEfINSsa4NWylLpmYVlwLL6_i4XVKmSUSttYjGE9wCwpadpJEdYryOgkUaVq155aW3ukoR4B5JAXUo1xGmss5FwoWzIUU2c3HBE5gyPN_k8YYI-oawp0F275FxQMbj8r3Jc1Qum65tGOGZ7iIiRFEEOm3kXzpogyjvGvIxOD3F8ddP0-ekHSq8SxaWRKbVkODlugyrhN5GHAjWHJDq005LhQQV3C6RT6eMmB-eKDKZRn8JrVpVjUsEM_NYl3HcC0O7NfMu8GIljn4oPLyddtMhlye5e7UQKsVRisN1RQrl2mwIKkRjUp-F6eb179S8QTWWxe-UQ2pTWxG_kpWAXTZYTLuW6QA2Z7emrta9E0m1NkYZKMj-2ebo1RLr0TPZuPl8W5zr0AIcInkoYOPU7PjGzct6maBDglfYETLT4uxKHq8FSzkJvE2jm6dBF2ZgE-HNvxryZgCSYQz37b6lrZxyzQp9JVU6qyDPeybr3Z8ljUkCyoOEsJ375S_9Sml5YvPZq8fBCg9nQoo7GYNPIpt8uhJGzuDRFTu1ETMb4u8oTnyOmlLvXkE4WN9tawsmbbX4YCN5E2KAnU1II4udETIG3efO4Me0zVr68hqUBU8jp_-2BHTSaDQQrSHlLC_Q4T_IgPZW3CdcEGTFmlBZsC3h5mA4QrAB5gcP8ThE_wYxySgcKhciFxnGWZJHiJ4w7ZoyKEHiH9ndK-UdAEPoYEWeb1bB2x1PY4c1sPJdnM0ul1Wb8U_I2X2Whc5aYTl64hvMaFDb2QPHRUaM3wvQfhA_5Q5k15ZShg4Y3dTpnXOkEbPIDg_Avdta0TEJcssYU1oGS-_fciEWvoU7MnKothfFp4Xk61efApIl190T-EaOg6usQ-01GUSqoWnomy8pfqXvlzAhCFgFUkqJ-b0Ip6P4h00HhQe1MF8Pvrw6BUT3xcarkeRg3nk5gdciZLdhPnNcx6wj5Aq_WI6kvLv1XmhLSiGC1xWtjFGZhTGwjIM30btVuGacbACBwszezkD1Uw3k29Q7wvMHdrFRt8i9Qhl1TcBy0FbQhZIEfFeDtLrdnR6EeWgN17GweCzSq0GCFFzsBj_YsDQ_9ZhgS_jAt4L29d1nLNmzRfIzQmENM-UuQoqYFgn2myzke2Jf9E0c9pl3OyeVxCUvIbqXoy7_tityD3MNB5zmD6_7z5l79hP9ku4fXKTtoFJ4nOB2Z4fr9gzGEl52Ev5ah7TZZao55strWmOYc3DuLZRUstmiMHV_X_DJyMDxuF0DDLVMyg2e3KJ0_1s40s1FOWL1dGo5u_TyUW4rir2pVyJqqlserMcbXq9XFVkjuPoa-vw1SHL6gcoRADTup61YoHShirAcjHHZtA7jHpmY9253z0WDAR3wdsh4D685Fo_LYdATonJZEZYQokPzoJ2-LTXRZVr-zlafdaiehxP0M6-BV_LEZgz7Zo_EUNWqSIApf263OOv5KXDfAsqJ_FH8VfHH-1zpCbVJOGdImittDmgpfOtqdq8wbwW73LgtbNgxYi03DpVJFgFwCgT4Qjm3m8qxEDoS2FBeSLlQpfz31oz3lh1nUC6bYbebHynEFWArE-6mukkl4heI5nQ4KZw5TAotj94zmHVnzOoq8_AM8FH8qEvK7pHeverjx8afnJR5Lzjvt9FA86uFG6LuW0RFxLrhIzO0W5DItGXcf75dz2LOp1vTeiL16qdnHmMY59e1Q5Br1U4PiYJaFzIREV3URKfvqS7WnBs0Bwh5O62zjEiwAc55OxU91FsssAV_YRR4jOxPTkE1b15Tb1fzxeqS63mnjRbNCjJu6zccIPU1AjUhtNRKQjmlJ9zRE7NunY_zx26L7prSWvf0z6PdHVNm0r0QOTCe20KuwckUjdIhiQEhcc6FSODofwkSgoYd63S1aQUG6v_flPBTKrbRBAF-3BoziO7H9h4IZxZ9U_d91GDbwJW-I4rY9p1lZK7843wiy8SgRjVthS7czvEPzdVvylQbWOBabiTn0C1iL71Ex6IrHShTsiW5YtQRaKrbt3Zqnhk94R6pPnsOJ8ZqIqYptyps3Ktfn2p-cVojP3EXY4Z_znOgZds7fbsYw1dAHFYBhBpyr6ue51DRgHxT3-ppWebfMJxOJnnlqdL5pAYNEkPq4fOkJQ82dczcF69K_WDbOc5FVKnV4jeyo1kCe8r9MuQMCf1fcd9nyjJGoyNlhcDBYOF_gjh2_7AdhRanet5fMcPmGeJy8zWwG_Grx8xV62a3hJDvpueuXuf-rzsfaWFSBIirNL0vfWx4Et7QBg55Bh57Uw5FWyUFkklEEzHc-nqAZgP0gQLvlFqZWhixU92HhMlfNWQpdLI5d_-aKMSjf-w5FrBbzT8yEQG5qLVL1M5tq9UpJOUX7W9R5Kko7AcPXFjneQ4w5Og9Cx2IFy9L7hIwhy4srY8U_gFTe72j3Wo-qSyXx0xL-3mNQIuwYnig6YzD4QmVuXWFUkRYdk8_dYkdOFS0Bhztr7j5UNAw0OylSvEtWVIShMu660CFvBHG8WIeXaZ66JlxDbH5lmywcMBu6mieev9y8Ryf6cpRBTPxZMqr6jWmP2MiyHNKX7Sd7gIWCMAutaxbG0J0F692QViu4OmRLQK7g7DqDxuEn3y4sFJyS-zBnlJLhCrCQXuQrvpkV2q4cydiAfpgPzpb9RFjvrMmsbll6pJu1CPKMuVfLZSGwquW2hLsZpxysIRwKZwfIgCCm0VFJbxpoDo_CrhpIlpndAt-6X7R5lL3O1ZJsQX4PX0f7UimS4FE4MDilGrep9p1ct8w2xte18CBJzbm6YAboI7yxOcwKAmVptlR6t9Ft5tnhhD1gieMj13epRi4alAIVLKebd8EOSUqiUWsz-lTDmZpfZUpWGnMli_jVRAewRW4zp6fFcMft_ibw_vFDuxoToUh4rx8AnQ2u5nij7CkwbBBw5jdeTBrB19ql09DtzWpkuWC4XUxsFMnsWnyu-y25M1BS7wWSzxLSCXvkU2Vf5HW3sl4kFPZ59loAyE31AgOVcjNzYi4cmXonUXan6P5KwjAXUckmFCkf8JzUNV4ZVlb8xqRHzkfgb_8IKh8xvsABIgp9Ul0m8WZY5zpo2Iz1ulnIbO_mPYXEsyMh7kKk2FbUglVmDZ6sQF32ILRHJulWHPYp3VXZ3PQOYHFDA1qaklX9qvu8HktlAiX9du_mOoHFJKBqrrhcLSaIXl8OqJGCVjPBUWb_TQjnS0js9sLfK_G32ZQ6IIeyInq5a4W4WfdV47Fm6W-xYZKoeQOIe7LEZAP1Bnnr77oPtWuULTe4_U25eMSlKmzTGUXidbQvMSYHMXlvI-5bnn41m-cglTYcKkGVnIgx4B-FQgWW5pmvffTL_QraUNAafs7pqRdhL-Pn4yXOcFFKUZQBxPEv5ntsB13ZNIVEPDp5hjsAcLWjbX1ERQGga1yIZ4a_WzU43eHnkA_PnaVuM-NHQaSPWAO1c4CnBY59X6EH14ouM0iBme5oSFjD4HPaHfACc0ivGMAm3tgr3mhVXjYtavhVq4p1d7vQMMJ83RwhuzB7s6mupPYrCd9ze-FIAGyOwA3BMCuEPuX02ivG7L0Ogr9cmP2CJ5oR-qepWnrAqpsRu_SxdUoPs06MImZecQOJIt97SYoXXgz0gISsVVZe49woP418y5W4djg4HYWoYV8eyrFHGJqYNm9g9oZjfF9AcjMiL0yyvoWX5yeQaAAnrePgmbZsHb2PivKkYR2SpX1R5v_ufb1QMDLdX8V7I7I5StY_8h_w=")
                .apply {

                    JSONObject(this).getJSONArray("detect_record").let {
                        for (index in 0..length){
                            kotlin.runCatching {
                                Log.e("SmileDon", it.getString(index))
                            }
                        }
                    }
                }
        }.start()*/
    }

}

data class DetectData(
    val play_times: Int,
    val record_type: Int,
    val task_id: String,
    val exe_plan_id: String,
    val device_id: String,
    val material_id: String,
    val date: String,
    val play_duration: String,
    val screen_attribute: String,
)