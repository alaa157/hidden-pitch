package hiddenpitch.ui.common

class AuthGate {
    val isSignedIn: Boolean = false

    fun openProtectedDestination(navigate: () -> Unit) {
        // M2 keeps placeholders reachable; M3 will add Google sign-in here.
        navigate()
    }
}
