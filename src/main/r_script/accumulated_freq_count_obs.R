library(dplyr)
library(ggplot2)
library(scales)
library(ggmosaic)
total_count <- 54683532
setwd("/Users/zhangx/git/HushToFhir")
concept_counts <- read.csv("./data/cpt_category_count.csv", header = TRUE, sep = ",", skip = 1)
concept_counts$index <- seq(nrow(concept_counts))
ggplot(concept_counts) + geom_bar(aes(x = index, y = count / total_count), stat = "identity") + 
  geom_text(aes(x = index, y = count / total_count, label = category, vjust = -2 +1  * (index + 1) %% 2, hjust = (index + 1) %% 2 * 0.4)) +
  scale_x_continuous(limit = c(0, 15)) + scale_y_continuous(limit = c(0, 0.25), labels = percent) + 
  xlab("concept category rank") + ylab("percentage frequency") + 
  theme_bw() + theme(panel.grid = element_blank())
ggsave("./images/concept_category_freq.pdf", width = 4.5, height = 4)
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
ggsave("./data/images/cumulative_freq_plot.pdf", width = 4, height = 5)


# make a mosaic plot to show the information in the dataset
concept_counts_top <- concept_counts %>% filter(index <= 9) %>% mutate(linecount = count) %>% select(-index)
tables <- c("PATIENTS", "PROVIDERS", "VISITS")
lineCount <- c(15682, 6986, 3471371)
hush_data_line_counts <- concept_counts_top %>% select(category, linecount) %>%
  bind_rows(data.frame(category = tables, linecount = lineCount)) %>%
  arrange(-linecount) %>%
  mutate(w = c(0.2, 0.2, 0.2, 0.1, 0.1, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05 ))

set.seed(121)
hush_data_line_counts %>% 
  mutate(x1 = rep(1:3, each = 4)) %>%
  mutate(x2 = rep(1:4, 3)) %>% 
  mutate(x3 = sample(1:6, 12, replace = TRUE)) %>%
  mutate(x1 = factor(x1), x2 = factor(x2), x3 = factor(x3)) %>%
  ggplot() + geom_mosaic(aes(weight = linecount, x = product(x1, x2), fill = x3), na.rm = TRUE) +
  xlab("") + ylab("") +
  theme_bw() + 
  theme(axis.ticks = element_blank(), axis.text = element_blank(), 
        panel.grid = element_blank(), panel.background = element_blank(),
        panel.border = element_blank(), legend.position = "none") +
  coord_flip()
  
ggsave("/Users/zhangx/git/HushToFhir/data/images/hush_dataset.png", width = 1.5, height = 1.5)

hush_data_line_counts$isUsed = c("Y", "Y", "N", "Y", "Y", "Y", "N", "N", "N", "N", "Y", "N")

data_volumes <- hush_data_line_counts %>% 
  ggplot() + 
  geom_bar(aes(x=fct_reorder(category, -linecount), y = linecount/1000000, fill = isUsed), 
           stat = "identity", color = "black", size = 0.1) +
  xlab("") + ylab("million lines") +
  scale_fill_manual(limits = c("N", "Y"), values = c("white", "cyan3")) +
  theme_bw() +
  theme(axis.text.x = element_text(angle = 90, size = 8), axis.text.y = element_text(size = 8),
        axis.title = element_text(size = 8),
        panel.grid = element_blank(), 
        legend.position = "none")
ggsave("./data/images/hush_dataset_bar.png", plot = data_volumes, width = 3, height = 2.5)
ggsave("./data/images/hush_dataset_bar.pdf", plot = data_volumes, width = 3, height = 2.5)
