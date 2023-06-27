package com.miscellaneoustool.app.legacy.Fragment

/*
@AndroidEntryPoint
class Login : BaseFragment<AlarmLoginBinding>() {
    val loginViewModel: LoginViewModel by viewModels()

    override fun getViewDataBinding(): AlarmLoginBinding = AlarmLoginBinding.inflate(layoutInflater)
    override var topBarVisibility: Boolean = true
    override var bottomNavigationBarVisibility: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginViewModel = loginViewModel
        binding.navController = Navigation.findNavController(view)

        loginViewModel.logFlag.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                Navigation.findNavController(view).popBackStack()
                loginViewModel.mutableLogFlag.value = false
            }
        })
    }
}

 */
