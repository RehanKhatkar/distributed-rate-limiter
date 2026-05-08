local key = KEYS[1]
local currentTime = tonumber(ARGV[1])
local windowSize = tonumber(ARGV[2])
local maxRequests = tonumber(ARGV[3])
local minScore = currentTime - windowSize
redis.call("ZREMRANGEBYSCORE", key, 0, minScore)
local requestCount = redis.call("ZCARD", key)
if requestCount < maxRequests then
    redis.call("ZADD", key, currentTime, currentTime)
    redis.call("EXPIRE", key, windowSize)
    return 1
end
return 0