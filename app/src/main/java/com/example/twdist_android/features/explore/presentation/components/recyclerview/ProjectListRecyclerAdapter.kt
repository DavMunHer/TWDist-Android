package com.example.twdist_android.features.explore.presentation.components.recyclerview

import android.graphics.drawable.GradientDrawable
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.twdist_android.R
import com.example.twdist_android.features.explore.presentation.model.ProjectUi

class ProjectListRecyclerAdapter :
    ListAdapter<ProjectUi, ProjectRowViewHolder>(Diff) {

    var onProjectClick: (ProjectUi) -> Unit = {}
    var onStarClick: (ProjectUi) -> Unit = {}
    var rowColors: ProjectRowColors = ProjectRowColors.defaultLightFallback()

    fun syncListAndColors(projects: List<ProjectUi>, colors: ProjectRowColors) {
        val colorChanged = rowColors != colors
        rowColors = colors
        submitList(projects) {
            if (colorChanged) {
                notifyItemRangeChanged(0, itemCount)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectRowViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_project_list_row, parent, false)
        return ProjectRowViewHolder(view, onProjectClick, onStarClick)
    }

    override fun onBindViewHolder(holder: ProjectRowViewHolder, position: Int) {
        holder.bind(getItem(position), onProjectClick, onStarClick, rowColors)
    }

    private object Diff : DiffUtil.ItemCallback<ProjectUi>() {
        override fun areItemsTheSame(oldItem: ProjectUi, newItem: ProjectUi): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ProjectUi, newItem: ProjectUi): Boolean =
            oldItem == newItem
    }
}

class ProjectRowViewHolder(
    private val itemRoot: View,
    private var onProjectClick: (ProjectUi) -> Unit,
    private var onStarClick: (ProjectUi) -> Unit
) : RecyclerView.ViewHolder(itemRoot) {
    private val nameView: TextView = itemRoot.findViewById(R.id.project_name)
    private val pendingView: TextView = itemRoot.findViewById(R.id.pending_count)
    private val starButton: ImageButton = itemRoot.findViewById(R.id.star_button)
    private val navigateIcon: ImageView = itemRoot.findViewById(R.id.navigate_icon)

    private var bound: ProjectUi? = null

    /** Same as [com.example.twdist_android.features.explore.presentation.components.ProjectCard] `remember { mutableStateOf(false) }`. */
    private var starVisualFilled: Boolean = false
    private var starVisualProjectId: Long = Long.MIN_VALUE

    init {
        itemRoot.setOnClickListener {
            bound?.let { onProjectClick(it) }
        }
        starButton.setOnClickListener {
            bound?.let { project ->
                starVisualFilled = !starVisualFilled
                applyStarVisual(rowColorsSnapshot())
                onStarClick(project)
            }
        }
        navigateIcon.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
    }

    private var lastRowColors: ProjectRowColors = ProjectRowColors.defaultLightFallback()

    private fun rowColorsSnapshot(): ProjectRowColors = lastRowColors

    fun bind(
        project: ProjectUi,
        projectClick: (ProjectUi) -> Unit,
        starClick: (ProjectUi) -> Unit,
        rowColors: ProjectRowColors
    ) {
        onProjectClick = projectClick
        onStarClick = starClick
        lastRowColors = rowColors
        if (starVisualProjectId != project.id) {
            starVisualProjectId = project.id
            starVisualFilled = false
        }
        bound = project

        val density = itemRoot.resources.displayMetrics.density
        val cornerPx = 12f * density
        itemRoot.background = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = cornerPx
            setColor(rowColors.surfaceVariant)
        }
        pendingView.background = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(rowColors.primary)
        }

        nameView.text = project.name
        nameView.setTextColor(rowColors.onSurface)
        pendingView.text = project.pendingTasks.toString()

        navigateIcon.imageTintList = ColorStateList.valueOf(rowColors.onSurface)

        applyStarVisual(rowColors)
    }

    private fun applyStarVisual(rowColors: ProjectRowColors) {
        val ctx = itemRoot.context
        val starGold = ContextCompat.getColor(ctx, R.color.star_gold)
        if (starVisualFilled) {
            starButton.setImageResource(R.drawable.ic_star_filled_24)
            starButton.imageTintList = ColorStateList.valueOf(starGold)
        } else {
            starButton.setImageResource(R.drawable.ic_star_outline_24)
            starButton.imageTintList = ColorStateList.valueOf(rowColors.onSurface)
        }
    }
}
