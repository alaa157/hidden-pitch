package hiddenpitch.util

import hiddenpitch.BuildConfig

object AppConfig {
    val supabaseUrl: String get() = BuildConfig.SUPABASE_URL
    val supabaseAnonKey: String get() = BuildConfig.SUPABASE_ANON_KEY
}
