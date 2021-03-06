require(randomForest)

setwd("~/Documents/VIMSS/ontology/NCATS/HUSH+/patient_feature/")

data <- read.csv("./Hush+_data_matrix.csv",sep=",",header=T,row.names=1)

#Aaron's path
setwd("~/git/HushToFhir")
data <- read.csv("./data/Hush+_data_matrix.csv",sep=",",header=T, row.names = c("patient_num"))
data$X = NULL

dim <- dim(data)
dim

male <- mat.or.vec(dim[1],1)
male[which(data[,1] == "M")] <- 1
female <- mat.or.vec( dim[1],1)
female[which(data[,1] == "F")] <- 1


data_all <- data

#remove sex column
#data <- data[,-1]
data$sex_cd = NULL

datanew <- cbind(female, male, data)
dim(datanew)

class(datanew)
colnames(datanew)
dim <- dim(datanew)
dim
num_patients <- dim[1]
num_samples <- dim[1]/2
#13985/2
#6992

randsample <- sample(num_patients,num_samples)

training <- datanew[randsample,]
dim(training)
test <- datanew[-randsample,]
colnames(training) <- c("male","female", colnames(data))
colnames(test) <-c("male","female", colnames(data))

set.seed(101)

sum(is.na(training))

training$isSevere <- as.character(training$isSevere)
training$isSevere <- as.factor(training$isSevere)

test$isSevere <- as.character(test$isSevere)
test$isSevere <- as.factor(test$isSevere)

#rm na
training <- na.omit(training)
testing <- na.omit(test)

#Aaron
n_pos = sum(training$isSevere == 1) #156
n_neg = sum(training$isSevere == 0) #1353
training_positive <- training[training$isSevere==1,]
#try to add more positive examples
#did not work
indexes_resample_pos <- sample(1:n_pos, n_neg-n_pos, replace = TRUE)
resampled_pos <- training_positive[indexes_resample_pos,]
training_balaned <- rbind(training, resampled_pos)


#Aaron
#try to remove negative examples
#not working either
training_negative <- training[training$isSevere==0,]
indexes_resample_neg <- sample(1:n_neg, n_pos)
resampled_neg <- training_negative[indexes_resample_neg,]
training_balaned <- rbind(training_positive, resampled_neg)

for (n_tree in seq(150, 500, 50)) {
  rf_classifier <- randomForest(isSevere ~ ., data=training, ntree=n_tree, mtry=sqrt(dim[2]), importance=TRUE, nodesize=2,proximity=TRUE)
  print(n_tree)
  print(rf_classifier)
}

for (min_nodesize in 1:5) {
  for (max_nodesize in min_nodesize + 1 : min_nodesize+5 ) {
    rf_classifier <- randomForest(isSevere ~ ., data=training, ntree=300, mtry=sqrt(dim[2]), importance=TRUE, nodesize=min_nodesize,maxnodes = max_nodesize, proximity=TRUE)
    print(n_tree)
    print(rf_classifier)
  }
}
rf_classifier <- randomForest(isSevere ~ ., data=training, ntree=200, mtry=sqrt(dim[2]), importance=TRUE, nodesize=2,proximity=TRUE)
rf_classifier
#OOB estimate of  error rate: 9.31%
#Rerun by Aaron with slightly different raw data: error rate is similar, but confusion matrix report far worse problem. 
#All cases are reported as negative
#Confusion matrix: 
#0 1 class.error
#0 1362 2 0.001466276
#1  156 3 0.981132075
varImpPlot(rf_classifier)

index_of_outcome <- 121 

colnames(test)[index_of_outcome]
prediction_for_table <- predict(rf_classifier,test[,-index_of_outcome])
table <- table(observed=test[,index_of_outcome],predicted=prediction_for_table)
library(caret)

result <- confusionMatrix(table)
precision <- result$byClass['Pos Pred Value']    
recall <- result$byClass['Sensitivity']
precision
recall
> precision
Pos Pred Value 
0.9949158 
> recall
Sensitivity 
0.9081943 


library(ROCR)
# Calculate the probability of new observations belonging to each class
# prediction_for_roc_curve will be a matrix with dimensions data_set_size x number_of_classes
prediction_for_roc_curve <- predict(rf_classifier,test[,-index_of_outcome],type="prob")

pretty_colours <- c("#F8766D","#00BA38","#619CFF")

true_values <- ifelse(test[,index_of_outcome]==1,1,0)
# Assess the performance of classifier for class[i]
pred <- prediction(prediction_for_roc_curve[,2],true_values)
perf <- performance(pred, "tpr", "fpr")

plot(perf,main="ROC Curve",col=pretty_colours) 

# Calculate the AUC and print it to screen
auc.perf <- performance(pred, measure = "auc")
print(auc.perf@y.values)
#0.7634957
