package com.kk.android.comvvmhelper.extension

import androidx.fragment.app.Fragment
import org.koin.androidx.scope.fragmentScope
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.KoinScopeComponent
import org.koin.core.scope.Scope

/**
 * @author kuky.
 * @description
 */
fun KoinScopeComponent.createScopeAndLink(): Scope {
    check(this is Fragment) { "only fragment can link scope to an activity" }
    return fragmentScope().apply {
        (activity as? KoinScopeComponent)?.let { linkTo(it.scope) }
    }
}

/**
 * inject lazily
 * @param qualifier - bean qualifier / optional
 * @param mode
 * @param parameters - injection parameters
 */
inline fun <reified T : Any> KoinScopeComponent.scopeInject(
    qualifier: Qualifier? = null,
    mode: LazyThreadSafetyMode = LazyThreadSafetyMode.SYNCHRONIZED,
    noinline parameters: ParametersDefinition? = null
) = lazy(mode) { get<T>(qualifier, parameters) }

/**
 * get given dependency
 * @param parameters - injection parameters
 */
inline fun <reified T : Any> KoinScopeComponent.get(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): T = scope.get(qualifier, parameters)