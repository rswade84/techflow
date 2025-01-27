document.addEventListener("DOMContentLoaded", () => {
    const ticketStatusCountsElement = document.getElementById("ticketStatusCounts");
    if (!ticketStatusCountsElement) return;

    let ticketStatusCounts = {};
    try {
        ticketStatusCounts = JSON.parse(ticketStatusCountsElement.textContent);
    } catch (error) {
        console.error("Error parsing ticket status counts:", error);
        return;
    }

    const graphContainer = document.getElementById("ticketStatusBarGraph");
    if (!graphContainer) return;

    const ctx = graphContainer.getContext("2d");
    let chartInstance;

    const formatStatusLabels = {
        NOT_STARTED: "Not Started",
        IN_PROGRESS: "In Progress",
        DELAYED: "Delayed",
        RESOLVED: "Resolved",
        CLOSED: "Closed",
    };

    // Defines fixed colors for each status
    const statusColors = {
        NOT_STARTED: "#fca103", // Orange
        IN_PROGRESS: "#79aff2", // Blue
        DELAYED: "#fcd95b", // Yellow
        RESOLVED: "#75c787", // Green
        CLOSED: "#fc8b8b", // Light Red
    };

    // Order of statuses
    const statusOrder = ["NOT_STARTED", "IN_PROGRESS", "DELAYED", "RESOLVED", "CLOSED"];

    const calculatePercentages = (data, includeClosed) => {
        const totalTickets = Object.keys(data)
            .filter(key => includeClosed || key !== "CLOSED")
            .reduce((sum, key) => sum + data[key], 0);

        return Object.keys(data).reduce((percentages, key) => {
            if (!includeClosed && key === "CLOSED") return percentages;
            percentages[key] = {
                percentage: ((data[key] / totalTickets) * 100).toFixed(2),
                count: data[key],
            };
            return percentages;
        }, {});
    };

    const renderChart = (percentages) => {
        // Sorts the statuses based on the desired order
        const sortedKeys = Object.keys(percentages).sort((a, b) => {
            return statusOrder.indexOf(a) - statusOrder.indexOf(b);
        });

        const labels = sortedKeys.map(status => formatStatusLabels[status] || status);
        const dataValues = sortedKeys.map(key => percentages[key].percentage);
        const ticketCounts = sortedKeys.map(key => percentages[key].count);

        // Maps colors to each status based on the order
        const colors = sortedKeys.map(status => statusColors[status] || "#CCCCCC");

        if (chartInstance) {
            chartInstance.destroy();
        }

        chartInstance = new Chart(ctx, {
            type: "bar",
            data: {
                labels: labels,
                datasets: [{
                    data: dataValues,
                    backgroundColor: colors,
                    borderRadius: 8,
                    barThickness: 25,
                }],
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                indexAxis: "y",
                plugins: {
                    legend: { display: false },
                    tooltip: {
                        callbacks: {
                            title: (context) => `${context[0].label}`,
                            label: (context) => [
                                `Percentage: ${context.raw}%`,
                                `Ticket Count: ${ticketCounts[context.dataIndex]}`,
                            ],
                        },
                    },
                },
                layout: {
                    padding: { top: 20, bottom: 10 },
                },
                scales: {
                    x: {
                        title: {
                            display: true,
                            text: "Percentage (%) of All Tickets",
                            font: { size: 12, weight: "bold" },
                            padding: { top: 10 },
                        },
                        ticks: {
                            font: { size: 10 },
                            maxRotation: 0,
                            autoSkip: true,
                        },
                        grid: { display: false },
                    },
                    y: {
                        ticks: {
                            font: { size: 12, weight: "bold" },
                            padding: 5,
                        },
                        grid: { display: false },
                    },
                },
            },
        });
    };

    const includeClosedToggle = document.getElementById("includeClosed");
    let percentages = calculatePercentages(ticketStatusCounts, includeClosedToggle.checked);
    renderChart(percentages);

    includeClosedToggle.addEventListener("change", () => {
        percentages = calculatePercentages(ticketStatusCounts, includeClosedToggle.checked);
        renderChart(percentages);
    });
});