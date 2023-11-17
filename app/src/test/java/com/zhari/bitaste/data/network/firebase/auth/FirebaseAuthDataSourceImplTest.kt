package com.zhari.bitaste.data.network.firebase.auth

import android.text.TextUtils
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.lang.Exception

class FirebaseAuthDataSourceImplTest {

    @MockK
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var dataSource: FirebaseAuthDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dataSource = FirebaseAuthDataSourceImpl(firebaseAuth)
    }

    private fun mockTaskAuthResult(exception: Exception? = null): Task<AuthResult> {
        val task: Task<AuthResult> = mockk(relaxed = true)
        every { task.isCanceled } returns false
        every { task.exception } returns exception
        every { task.isComplete } returns true

        val relaxedResult = mockk<AuthResult>(relaxed = true)
        every { task.result } returns relaxedResult
        every { task.result.user } returns mockk(relaxed = true)
        return task
    }

    private fun mockTaskVoid(exception: Exception? = null): Task<Void> {
        val task: Task<Void> = mockk(relaxed = true)
        every { task.isCanceled } returns false
        every { task.exception } returns exception
        every { task.isComplete } returns true

        val relaxedVoid = mockk<Void>(relaxed = true)
        every { task.result } returns relaxedVoid
        return task
    }

    @Test
    fun `test login`() {
        every { firebaseAuth.signInWithEmailAndPassword(any(), any()) } returns mockTaskAuthResult()
        runTest {
            val result = dataSource.doLogin("email", "password")
            assertEquals(result, true)
        }
    }

    @Test
    fun `test register`() {
        runTest {
            mockkConstructor(UserProfileChangeRequest.Builder::class)
            every { anyConstructed<UserProfileChangeRequest.Builder>().build() } returns mockk()

            val mockAuthResult = mockTaskAuthResult()
            every {
                firebaseAuth.createUserWithEmailAndPassword(
                    any(),
                    any()
                )
            } returns mockAuthResult

            val mockUser = mockk<FirebaseUser>(relaxed = true)
            every { mockAuthResult.result.user } returns mockUser

            every { mockUser.updateProfile(any()) } returns mockTaskVoid()

            val result = dataSource.doRegister("name", "email", "password")
            assertEquals(result, true)
        }
    }

    @Test
    fun testLogout() {
        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance() } returns firebaseAuth
        every { firebaseAuth.signOut() } returns Unit
        val result = dataSource.doLogout()
        assertEquals(result, true)
    }

    @Test
    fun getCurrentUser() {
        val user = mockk<FirebaseUser>(relaxed = true)
        every { firebaseAuth.currentUser } returns user
        val result = dataSource.getCurrentUser()
        assertEquals(result, user)
        verify { firebaseAuth.currentUser } // cek every sudah kepanggil belum
    }

    @Test
    fun isLoggedIn() {
        val user = mockk<FirebaseUser>(relaxed = true)
        every { firebaseAuth.currentUser } returns user
        val result = dataSource.isLoggedIn()
        assertEquals(result, true)
        verify { firebaseAuth.currentUser }
    }

    @Test
    fun sendChangePasswordRequestByEmail() {
        every { firebaseAuth.currentUser?.email } returns "farhan@gmail.com"
        every { firebaseAuth.sendPasswordResetEmail(any()) } returns mockTaskVoid()
        val result = dataSource.sendChangePasswordRequestByEmail()
        assertEquals(result, true)
        verify { firebaseAuth.sendPasswordResetEmail(any()) }
    }

    @Test
    fun updateEmail() {
        every { firebaseAuth.currentUser?.updateEmail(any()) } returns mockTaskVoid()
        runTest {
            val result = dataSource.updateEmail("farhan@gmail.com")
            assertEquals(result, true)
            verify { firebaseAuth.currentUser?.updateEmail(any()) }
        }
    }

    @Test
    fun updatePassword() {
        every { firebaseAuth.currentUser?.updatePassword(any()) } returns mockTaskVoid()
        runTest {
            val result = dataSource.updatePassword("farhan123")
            assertEquals(result, true)
            verify { firebaseAuth.currentUser?.updatePassword(any()) }
        }
    }

    @Test
    fun updateProfile() {
        mockkStatic(TextUtils::class)
        coEvery { TextUtils.isEmpty(any()) } returns true
        every { firebaseAuth.currentUser?.updateProfile(any()) } returns mockTaskVoid()
        runTest {
            val result = dataSource.updateProfile("Farhan")
            assertEquals(result, true)
            verify { firebaseAuth.currentUser?.updateProfile(any()) }
        }
    }
}
