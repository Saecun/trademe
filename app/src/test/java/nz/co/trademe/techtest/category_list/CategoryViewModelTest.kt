package nz.co.trademe.techtest.category_list

import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.operators.single.SingleJust
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import nz.co.trademe.techtest.util.Networking
import nz.co.trademe.wrapper.TradeMeApi
import nz.co.trademe.wrapper.TradeMeApiService
import nz.co.trademe.wrapper.models.Category
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.validateMockitoUsage
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


@RunWith(PowerMockRunner::class)
@PrepareForTest(CategoryViewModel::class, TradeMeApi::class, SingleJust::class)
class CategoryViewModelTest {

    @Mock
    lateinit var mockApi: TradeMeApi

    @Mock
    lateinit var mockService: TradeMeApiService

    private val categoryViewModel = CategoryViewModel()

    private inner class CategoryFetchListenerImpl: CategoryFetchListener {
        override fun onError(error: CategoryFetchListener.Error) {}

        override fun onSuccess(categoryList: List<Category>?) {}
    }


    @Before
    fun before() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }

        PowerMockito.whenNew(TradeMeApi::class.java).withAnyArguments().thenReturn(mockApi)
        Mockito.`when`(mockApi.get()).thenReturn(mockService)
    }

    @After
    fun validate() {
        validateMockitoUsage()
    }

    @Test
    fun getCategories_timeout() {
        //ARRANGE
        val single = PowerMockito.spy(Single.just(Category("","",false)))

        Mockito.`when`(mockService.getCategory(Mockito.anyString())).thenReturn(single)

        //ACT
        categoryViewModel.getCategories(CategoryFetchListenerImpl())

        //ASSERT
        Mockito.verify(mockService).getCategory("0")
        Mockito.verify(single).timeout(Networking.TIMEOUT_SECONDS, TimeUnit.SECONDS)
    }

    @Test
    fun getCategories_exception() {
        //ARRANGE
        val errorSingle = Single.error<Category>(TimeoutException())
        Mockito.`when`(mockService.getCategory(Mockito.anyString())).thenReturn(errorSingle)

        val categoryFetchListener = Mockito.mock(CategoryFetchListenerImpl::class.java)

        //ACT
        categoryViewModel.getCategories(categoryFetchListener)

        //ASSERT
        Mockito.verify(mockService).getCategory("0")
        Mockito.verify(categoryFetchListener).onError(CategoryFetchListener.Error.UNKNOWN)
        Mockito.verifyNoMoreInteractions(categoryFetchListener)
    }

    @Test
    fun getCategories_hierarchy() {
        //ARRANGE
        val subSubCategory1 = Category("SubSubCatId", "SubSubCatName", false, Collections.emptyList())
        val subCategory1 = Category("SubCatId", "SubCatName", true, Collections.singletonList(subSubCategory1))
        val subCategory2 = Category("SubCatId2", "SubCatName2", false, Collections.emptyList())
        val rootCategory = Category("RootId", "Name", false, listOf(subCategory1, subCategory2))


        val single = Single.just(rootCategory)
        Mockito.`when`(mockService.getCategory(Mockito.anyString())).thenReturn(single)

        val categoryFetchListener = Mockito.mock(CategoryFetchListenerImpl::class.java)

        //ACT
        categoryViewModel.getCategories(categoryFetchListener)


        //ASSERT
        Mockito.verify(mockService).getCategory("0")

        @Suppress("UNCHECKED_CAST")
        val capture = ArgumentCaptor.forClass(List::class.java) as ArgumentCaptor<List<Category>>

        Mockito.verify(categoryFetchListener).onSuccess(capture.capture())
        Mockito.verifyNoMoreInteractions(categoryFetchListener)

        Assert.assertEquals(subCategory1.id, capture.value[0].id)
        Assert.assertEquals(subCategory1.name, capture.value[0].name)
        Assert.assertEquals(subCategory1.isLeaf, capture.value[0].isLeaf)
        Assert.assertEquals(subCategory1.subcategories, capture.value[0].subcategories)
        Assert.assertEquals(subCategory2.id, capture.value[1].id)
        Assert.assertEquals(subCategory2.name, capture.value[1].name)
        Assert.assertEquals(subCategory2.isLeaf, capture.value[1].isLeaf)
        Assert.assertEquals(subCategory2.subcategories, capture.value[1].subcategories)
    }
}