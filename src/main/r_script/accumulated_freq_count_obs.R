library(dplyr)
library(ggplot2)
library(scales)
total_count <- 54683532
cpt_category_counts <- read.csv("/Users/zhangx/git/HushToFhir/data/cpt_category_count.csv", header = TRUE, sep = ",", skip = 1)
cpt_category_counts$index <- seq(nrow(cpt_category_counts))
ggplot(cpt_category_counts) + geom_bar(aes(x = index, y = count / total_count), stat = "identity") + 
  geom_text(aes(x = index, y = count / total_count, label = category, vjust = -2 +1  * (index + 1) %% 2, hjust = (index + 1) %% 2 * 0.4)) +
  scale_x_continuous(limit = c(0, 15)) + scale_y_continuous(limit = c(0, 0.25), labels = percent) + 
  xlab("concept category rank") + ylab("percentage frequency") + 
  theme_bw() + theme(panel.grid = element_blank())
ggsave("concept_category_freq.pdf", width = 4.5, height = 4)
## unfortunately, you need to type in the hard address of the dataset
## @TODO: checkout ways to get the directory of current script
data <- read.csv("/Users/zhangx/git/HushToFhir/data/cumulative_cpt_count.csv", header = TRUE, sep = ",", skip = 1)
## total cpt count: 54683532
## create a cumulated count graph
total_count <- 54683532
ggplot(data) + geom_line(aes(x = step, y = count / total_count), color = "blue") +
  geom_point(aes(x = step, y = count / total_count), color = "red", shape = 1, size = 2) + 
  scale_y_continuous(labels = percent) +
  xlab("most frequent concepts") + ylab("cumulative freq (% of total)") + ggtitle("Cumulative freq of top 500 concepts") +
  theme_bw() + theme(panel.grid = element_blank())
ggsave("cumulative_freq_plot.pdf", width = 4, height = 5)
