package com.example.accountbookforme.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.accountbookforme.activity.DetailActivity
import com.example.accountbookforme.adapter.ItemListAdapter
import com.example.accountbookforme.databinding.FragmentListWithTitleBinding
import com.example.accountbookforme.model.Item
import com.example.accountbookforme.viewmodel.CategoriesViewModel

class CategoryFragment : Fragment() {

    private var _binding: FragmentListWithTitleBinding? = null
    private val binding get() = _binding!!

    private val categoriesViewModel: CategoriesViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var itemListAdapter: ItemListAdapter

    private var categoryId: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentListWithTitleBinding.inflate(inflater, container, false)
        val view = binding.root

        // タイトルを表示
        binding.listTitle.text = "Items"

        recyclerView = binding.listWithTitle
        itemListAdapter = ItemListAdapter()

        // クリックした品物が登録されている支出詳細を表示する
        itemListAdapter.setOnItemClickListener(
            object : ItemListAdapter.OnItemClickListener {
                override fun onItemClick(item: Item) {

                    if (item.expenseId != null) {

                        val intent = Intent(context, DetailActivity::class.java)
                        // 支出IDを渡す
                        intent.putExtra("expenseId", item.expenseId!!)
                        // 支出詳細画面に遷移する
                        startActivity(intent)
                    }
                }
            }
        )

        val linearLayoutManager = LinearLayoutManager(view.context)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = itemListAdapter

        // セルの区切り線表示
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                view.context,
                linearLayoutManager.orientation
            )
        )

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 前画面から渡された支出IDを取得
        val bundle = arguments
        if (bundle != null) {
            categoryId = bundle.get("categoryId") as Long?
        }

        if (categoryId != null) {

            // 品物リスト取得
            categoriesViewModel.getItemListByCategoryId(categoryId!!)

            // 支出リストの監視開始
            categoriesViewModel.itemList.observe(viewLifecycleOwner, { itemList ->
                itemListAdapter.setItemListItems(itemList)
                // 総額を表示
                binding.allTotal.text = categoriesViewModel.calcItemTotal().toString()
            })
        }
    }


    // メニュータップ時の処理設定
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
        return true
    }
}