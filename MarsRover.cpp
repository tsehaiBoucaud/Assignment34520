#include <algorithm>
#include <chrono>
#include <iostream>
#include <mutex>
#include <random>
#include <thread>
#include <vector>

std::mutex secure_mtx;
std::default_random_engine
    rng(std::chrono::system_clock::now().time_since_epoch().count());
std::uniform_real_distribution<double> rng_dist(-100.0, 70.0);
double generateRandomTemp() {
  std::lock_guard<std::mutex> guard(secure_mtx);
  return rng_dist(rng);
}

class TemperatureSensor {
public:
  std::vector<double> tempData;
  TemperatureSensor() : tempData(60) {}
  void logTemperature(int index) { tempData[index] = generateRandomTemp(); }
  void logTemperatures() {
    for (int i = 0; i < 60; ++i) {
      this->logTemperature(i);
    }
  }
};

class TemperatureReport {
public:
  std::vector<TemperatureSensor> sensorArray;
  TemperatureReport(int size) : sensorArray(size) {}
  void generateReport() {
    std::vector<std::thread> threadPool;
    for (auto &sensor : sensorArray)
      threadPool.push_back(
          std::thread(&TemperatureSensor::logTemperatures, &sensor));
    for (auto &t : threadPool)
      t.join();

    std::vector<double> consolidatedTempData;
    for (auto &sensor : sensorArray)
      consolidatedTempData.insert(consolidatedTempData.end(),
                                  sensor.tempData.begin(),
                                  sensor.tempData.end());

    std::sort(consolidatedTempData.begin(), consolidatedTempData.end());

    std::cout << "Top 5 temperatures: ";
    for (int i = consolidatedTempData.size() - 5;
         i < consolidatedTempData.size(); i++) {
      std::cout << consolidatedTempData[i] << ", ";
    }
    std::cout << "\n";

    std::cout << "Bottom 5 temperatures: ";
    for (int i = 0; i < 5; i++) {
      std::cout << consolidatedTempData[i] << ", ";
    }

    int diffIndex = 0;
    double diffMax = 0.0;
    for (int i = 0; i < 50; i++) {
      double minTemp = *std::min_element(consolidatedTempData.begin() + i,
                                         consolidatedTempData.begin() + i + 10);
      double maxTemp = *std::max_element(consolidatedTempData.begin() + i,
                                         consolidatedTempData.begin() + i + 10);
      if (maxTemp - minTemp > diffMax) {
        diffMax = maxTemp - minTemp;
        diffIndex = i;
      }
    }
    std::cout << "Biggest temperature difference started at " << diffIndex
              << ". The difference was " << diffMax << "\n";
  }
};

int main() {
  TemperatureReport tempReport(8);
  tempReport.generateReport();
  return 0;
}