#!/usr/bin/env Rscript

args <- commandArgs(trailingOnly = TRUE)


if (length(args) != 6){
    cat("ERROR IN PASSING PARAMETERS: EXPECTED 5")
    stop("ERROR IN PASSING PARAMETERS: EXPECTED 5")
}

if("tsDyn" %in% rownames(installed.packages()) == FALSE) {install.packages("tsDyn", dependencies = TRUE, repos="http://cran.us.r-project.org")}
library("tsDyn")

file = args[1]
NUM_FORECAST = strtoi(args[2])
NOW = strtoi(args[3])
MAX = strtoi(args[4])
file_output = args[5]
interval = strtoi(args[6])
#
#file = "/home/mcanuto/BSC/Projects/RenewIT/RES_2015_short.csv"
# type = "green"
# NUM_FORECAST = window #Number of predicted values
# MAX = 100 #Max number of past values to consider
# NOW = 2 #Current time index

FREQUENCY = 1
cat(args)
data <- read.csv(file, check.names=FALSE)
data <- tail(data, MAX)
data <- data[complete.cases(data), ]
data <- rbind(c(0,0) ,data)


#cat("Input power: ")
#cat (data$power , "\n")
power.ts = data$power

sensor<- ts(power.ts,frequency=FREQUENCY)
mod.nnet <- linear(sensor, m=FREQUENCY)
fcast.values = predict(mod.nnet, n.ahead = NUM_FORECAST)
fcast.values[fcast.values < 0] <- 0
fcast.values[fcast.values > 1000000000] <- 1000000000

#cat("Output power: ", fcast.values, "\n")
#cat(fcast.values, sep = "\n")

time = seq(from=(NOW+FREQUENCY*interval), to=(NOW+NUM_FORECAST*interval), by=interval)

result_file = data.frame(cbind(timestamp = time, value = fcast.values))

write.csv(result_file, file = file_output, row.names=FALSE)

