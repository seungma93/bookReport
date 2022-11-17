package com.example.bookreport.presenter.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.bookreport.data.entity.BookAndBookMark
import com.example.bookreport.data.entity.room.Report
import com.example.bookreport.databinding.FragmentBookSearchBinding
import com.example.bookreport.databinding.FragmentReportEditBinding

class ReportEditFragment: Fragment() {
    companion object {
        const val REPORT_KEY = "REPORT_KEY"
    }
private lateinit var binding: FragmentReportEditBinding
private val report get() = requireArguments().getSerializable(ReportEditFragment.REPORT_KEY) as Report

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReportEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            bookTitle.text = report.title
            Glide.with(requireContext()).load(report.thumbnail).into(bookThumbnail)
            reportContext.setText(report.context)

        }
    }
}