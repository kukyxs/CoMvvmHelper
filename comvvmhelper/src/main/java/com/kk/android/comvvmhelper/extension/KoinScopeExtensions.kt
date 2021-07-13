package com.kk.android.comvvmhelper.extension

import org.koin.core.component.KoinScopeComponent
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

/**
 * @author kuky.
 * @description
 */

@Deprecated("replace by AndroidScopeComponent.inject", ReplaceWith("AndroidScopeComponent.inject"))
fun KoinScopeComponent.createScopeAndLink() {
//    check(this is Fragment) { "only fragment can link scope to an activity" }
//    return fragmentScope().apply {
////        (activity as? AndroidScopeComponent)/*?.let { linkTo(it.scope) }*/
//        activity?.getScopeOrNull()?.let {  }
//    }
}


/**
 * inject lazily
 * @param qualifier - bean qualifier / optional
 * @param mode
 * @param parameters - injection parameters
 */
@Deprecated("replace by AndroidScopeComponent.inject", ReplaceWith("AndroidScopeComponent.inject"))
inline fun <reified T : Any> KoinScopeComponent.scopeInject(
    qualifier: Qualifier? = null,
    mode: LazyThreadSafetyMode = LazyThreadSafetyMode.SYNCHRONIZED,
    noinline parameters: ParametersDefinition? = null
) = lazy(mode) { get<T>(qualifier, parameters) }

/**
 * get given dependency
 * @param parameters - injection parameters
 */
@Deprecated("replace by AndroidScopeComponent.inject", ReplaceWith("AndroidScopeComponent.get"))
inline fun <reified T : Any> KoinScopeComponent.get(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): T = scope.get(qualifier, parameters)