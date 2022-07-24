package com.binar.secondhand.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.binar.secondhand.DataDummy
import com.binar.secondhand.MainDispatcherRule
import com.binar.secondhand.data.BuyerRepository
import com.binar.secondhand.data.SellerCategoryRepository
import com.binar.secondhand.data.source.local.entity.BuyerProductEntity
import com.binar.secondhand.getOrAwaitValue
import com.binar.secondhand.ui.common.ProductAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()
    @Mock
    private lateinit var buyerRepository: BuyerRepository

    @Mock
    private lateinit var sellerCategoryRepository: SellerCategoryRepository

    @Test
    fun `when Get Quote Should Not Null and Return Success`() = runTest {
        val dummyQuote = DataDummy.generateDummyProductResponse()
        val data: PagingData<BuyerProductEntity> = ProductPagingSource.snapshot(dummyQuote)
        val expectedQuote = MutableLiveData<PagingData<BuyerProductEntity>>()
        expectedQuote.value = data
        Mockito.`when`(buyerRepository.getBuyerProducts(0)).thenReturn(expectedQuote)

        val homeViewModel = HomeViewModel(buyerRepository, sellerCategoryRepository)
        val actualProduct: PagingData<BuyerProductEntity> = homeViewModel.buyerProducts.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = ProductAdapter.ProductDiffCallBack,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualProduct)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyQuote, differ.snapshot())
        Assert.assertEquals(dummyQuote.size, differ.snapshot().size)
        Assert.assertEquals(dummyQuote[0].name, differ.snapshot()[0]?.name)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}

class ProductPagingSource : PagingSource<Int, LiveData<List<BuyerProductEntity>>>() {
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<BuyerProductEntity>>>): Int = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<BuyerProductEntity>>> =
        LoadResult.Page(emptyList(), 0, 1)

    companion object {
        fun snapshot(items: List<BuyerProductEntity>): PagingData<BuyerProductEntity> {
            return PagingData.from(items)
        }
    }
}