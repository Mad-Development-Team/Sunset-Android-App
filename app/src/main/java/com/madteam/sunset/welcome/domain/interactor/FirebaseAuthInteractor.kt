package com.madteam.sunset.welcome.domain.interactor

import com.google.firebase.auth.AuthResult
import com.madteam.sunset.common.utils.Resource
import com.madteam.sunset.welcome.data.FirebaseAuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface FirebaseAuthInteractorContract {

  fun doSignUp(email: String, password: String): Flow<Resource<AuthResult>>
  fun doSignInWithPasswordAndEmail(email: String, password: String): Flow<Resource<AuthResult>>
}

class FirebaseAuthInteractor @Inject constructor(
  private val firebaseAuthRepository: FirebaseAuthRepository
) :
  FirebaseAuthInteractorContract {

  override fun doSignUp(email: String, password: String): Flow<Resource<AuthResult>> =
    firebaseAuthRepository.doSignUp(email, password)

  override fun doSignInWithPasswordAndEmail(email: String, password: String): Flow<Resource<AuthResult>> =
    firebaseAuthRepository.doSignInWithPasswordAndEmail(email, password)

}