library(dplyr)
library(ggplot2)
library(scales)

## unfortunately, you need to type in the hard address of the dataset
## @TODO: checkout ways to get the directory of current script
data <- read.csv("/Users/zhangx/git/HushToFhir/data/accumulated_cpt_count.csv", header = TRUE, sep = ",")
## total cpt count: 54683532
## create a cumulated count graph
total_count <- 54683532
ggplot(data) + geom_line(aes(x = step, y = count / total_count), color = "blue") +
  geom_point(aes(x = step, y = count / total_count), color = "red", shape = 1, size = 2) + 
  scale_y_continuous(labels = percent) +
  xlab("most frequent concepts") + ylab("cumulative freq (% of total)") + ggtitle("Cumulative freq of top 500 concepts") +
  theme_bw() + theme(panel.grid = element_blank())
ggsave("cumulative_freq_plot.pdf", width = 4, height = 5)
