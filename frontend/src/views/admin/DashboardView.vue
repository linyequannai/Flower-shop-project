<template>
  <div>
    <h2>数据仪表盘</h2>
    <!-- 统计卡片 -->
    <el-row :gutter="20" style="margin-bottom:20px">
      <el-col :span="6"><el-card><div class="stat-card"><p class="stat-label">总营收</p><p class="stat-value">¥{{ dash.totalRevenue }}</p></div></el-card></el-col>
      <el-col :span="6"><el-card><div class="stat-card"><p class="stat-label">总订单</p><p class="stat-value">{{ dash.orderCount }}</p></div></el-card></el-col>
      <el-col :span="6"><el-card><div class="stat-card"><p class="stat-label">用户数</p><p class="stat-value">{{ dash.userCount }}</p></div></el-card></el-col>
      <el-col :span="6"><el-card><div class="stat-card"><p class="stat-label">在售花卉</p><p class="stat-value">{{ dash.flowerCount }}</p></div></el-card></el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="6"><el-card><div class="stat-card"><p class="stat-label">今日订单</p><p class="stat-value">{{ dash.todayOrderCount }}</p></div></el-card></el-col>
      <el-col :span="6"><el-card><div class="stat-card"><p class="stat-label">今日营收</p><p class="stat-value">¥{{ dash.todayRevenue }}</p></div></el-card></el-col>
    </el-row>
    <!-- 图表 -->
    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="12">
        <el-card><template #header>销售趋势</template><v-chart :option="trendOption" style="height:300px" /></el-card>
      </el-col>
      <el-col :span="12">
        <el-card><template #header>分类销售占比</template><v-chart :option="categoryOption" style="height:300px" /></el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="24">
        <el-card><template #header>畅销花卉 TOP10</template><v-chart :option="topOption" style="height:300px" /></el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart, PieChart, BarChart } from 'echarts/charts'
import { TitleComponent, TooltipComponent, LegendComponent, GridComponent } from 'echarts/components'

use([CanvasRenderer, LineChart, PieChart, BarChart, TitleComponent, TooltipComponent, LegendComponent, GridComponent])

import { getDashboard, getSalesTrend, getCategorySales, getTopFlowers } from '../../api/statistics'
import { formatPrice } from '../../utils/format'

const dash = ref({ totalRevenue: 0, orderCount: 0, userCount: 0, flowerCount: 0, todayOrderCount: 0, todayRevenue: 0 })
const trendData = ref([])
const categoryData = ref([])
const topData = ref([])

onMounted(async () => {
  dash.value = await getDashboard()
  trendData.value = await getSalesTrend('month')
  categoryData.value = await getCategorySales()
  topData.value = await getTopFlowers(10)
})

const trendOption = computed(() => ({
  tooltip: { trigger: 'axis' },
  xAxis: { type: 'category', data: trendData.value.map(d => d.date).reverse() },
  yAxis: { type: 'value' },
  series: [{ name: '销售额', type: 'line', data: trendData.value.map(d => d.amount).reverse(), smooth: true, itemStyle: { color: '#e74c8b' } }]
}))

const categoryOption = computed(() => ({
  tooltip: { trigger: 'item' },
  series: [{
    type: 'pie', radius: ['40%', '70%'],
    data: categoryData.value.map(d => ({ name: d.categoryName, value: d.amount })),
    emphasis: { itemStyle: { shadowBlur: 10, shadowOffsetX: 0 } }
  }]
}))

const topOption = computed(() => ({
  tooltip: { trigger: 'axis' },
  xAxis: { type: 'category', data: topData.value.map(d => d.date) },
  yAxis: { type: 'value' },
  series: [{ name: '销量', type: 'bar', data: topData.value.map(d => d.orderCount), itemStyle: { color: '#e74c8b' } }]
}))
</script>

<style scoped>
.stat-card { text-align: center; padding: 10px 0; }
.stat-label { font-size: 14px; color: #999; }
.stat-value { font-size: 28px; font-weight: bold; color: #333; margin-top: 8px; }
</style>
