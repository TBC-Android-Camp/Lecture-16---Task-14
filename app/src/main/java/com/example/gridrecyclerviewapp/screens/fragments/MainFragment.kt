package com.example.gridrecyclerviewapp.screens.fragments

import android.app.AlertDialog
import android.app.Dialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.gridrecyclerviewapp.R
import com.example.gridrecyclerviewapp.adapter.MainRecyclerViewAdapter
import com.example.gridrecyclerviewapp.base.BaseFragment
import com.example.gridrecyclerviewapp.databinding.DialogEditUserBinding
import com.example.gridrecyclerviewapp.databinding.FragmentMainBinding
import com.example.gridrecyclerviewapp.model.Human
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch


class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var humansAdapter: MainRecyclerViewAdapter

    override fun init() {
        setUpRecyclerView()
    }

    override fun listeners() {
        with(binding) {
            buttonAdd.setOnClickListener {
                checkValidation()
                clearAll()
            }

            swipeRefreshLayout.setOnRefreshListener {
                clearAll()
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun setUpRecyclerView() {
        humansAdapter = MainRecyclerViewAdapter()
        binding.recyclerView.adapter = humansAdapter

        humansAdapter.onDeleteClick { user -> deleteUserOperation(user) }
        humansAdapter.onEditClick { user -> showDialog(user) }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.humansFlow.collect { humans ->
                humansAdapter.submitList(humans)
            }
        }
    }

    private fun addUserOperation() {
        with(binding) {
            val logo = if (genderSwitch.isChecked) R.drawable.ic_female else R.drawable.ic_male
            viewModel.addNewUser(
                etName.text.toString(), etJob.text.toString(), genderSwitch.isChecked, logo
            )
        }
    }

    private fun deleteUserOperation(user: Human) {
        viewModel.deleteUser(user)
    }



    private fun showDialog(user: Human) {
        val dialogBinding = DialogEditUserBinding.inflate(layoutInflater)

        with(dialogBinding) {
            etEditName.setText(user.name)
            etEditJob.setText(user.job)
            genderEditSwitch.isChecked = user.gender
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("მონაცემების შეცვლა")
            .setView(dialogBinding.root)
            .setPositiveButton("შენახვა", null)
            .setNegativeButton("გაუქმება", null)
            .create()

        dialog.setOnShowListener {
            val positiveButton = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                with(dialogBinding) {
                    val newName = etEditName.text.toString()
                    val newJob = etEditJob.text.toString()

                    if (newName.isEmpty() || newJob.isEmpty()) {
                        Snackbar.make(
                            dialogBinding.root,
                            "სახელი და პროფესია არ უნდა იყოს ცარიელი!",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    } else {
                        viewModel.updateUser(
                            user.copy(
                                name = newName,
                                job = newJob,
                                gender = genderEditSwitch.isChecked
                            )
                        )
                        dialog.dismiss()
                    }
                }
            }
        }
        dialog.show()
    }



    private fun clearAll() {
        with(binding) {
            etName.text!!.clear()
            etJob.text!!.clear()
            genderSwitch.isChecked = false
        }
    }

    private fun checkValidation() = with(binding) {
        if (etName.text!!.isEmpty() || etJob.text!!.isEmpty()) Snackbar.make(
            binding.root, "შეავსეთ ველები!!", Snackbar.LENGTH_LONG
        ).show()
        else addUserOperation()
    }
}