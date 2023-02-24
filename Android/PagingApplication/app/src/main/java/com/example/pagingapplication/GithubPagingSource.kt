package com.example.pagingapplication

import androidx.paging.PagingSource
import com.example.pagingapplication.dao.Repo

class GithubPagingSource :PagingSource<Int, Repo>{
}