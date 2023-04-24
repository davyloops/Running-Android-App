package com.example.recordkeeper

data class Run(var id: String, var date: String, var distance: Float, var time: Int) {
    var averageSpeed = distance * time
}

data class Runs(var runs: MutableList<Run>) {
    var size: Int = runs.size

    operator fun get(position: Int): Run {
        return runs[position]
    }

    fun add(run: Run) {
        runs.add(run)
        size += 1
    }

    fun remove(run: Run) {
        val didRemoveRun = runs.remove(run)
        if (didRemoveRun) size -= 1
    }

    fun clear() {
        runs = mutableListOf<Run>()
        size = 0
    }
}