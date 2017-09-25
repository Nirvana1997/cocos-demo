#include "HelloWorldScene.h"
#include "SimpleAudioEngine.h"

USING_NS_CC;

Scene* HelloWorld::createScene()
{
    return HelloWorld::create();
}

// on "init" you need to initialize your instance
bool HelloWorld::init()
{
    //////////////////////////////
    // 1. super init first
    if ( !Scene::init() )
    {
        return false;
    }
    
    auto visibleSize = Director::getInstance()->getVisibleSize();
    Vec2 origin = Director::getInstance()->getVisibleOrigin();

    /////////////////////////////
    // 2. add a menu item with "X" image, which is clicked to quit the program
    //    you may modify it.

    // add a "close" icon to exit the progress. it's an autorelease object
    auto closeItem = MenuItemImage::create(
                                           "CloseNormal.png",
                                           "CloseSelected.png",
                                           CC_CALLBACK_1(HelloWorld::menuCloseCallback, this));
    
    closeItem->setPosition(Vec2(origin.x + visibleSize.width - closeItem->getContentSize().width/2 ,
                                origin.y + closeItem->getContentSize().height/2));

    // create menu, it's an autorelease object
    auto menu = Menu::create(closeItem, NULL);
    menu->setPosition(Vec2::ZERO);
    this->addChild(menu, 1);

    /////////////////////////////
    // 3. add your codes below...

    // add a label shows "Hello World"
    // create and initialize a label
    
    auto label = Label::createWithTTF("Hello World", "fonts/Marker Felt.ttf", 24);
    
    // position the label on the center of the screen
    label->setPosition(Vec2(origin.x + visibleSize.width/2,
                            origin.y + visibleSize.height - label->getContentSize().height));

    // add the label as a child to this layer
    this->addChild(label, 1);

//    // add "HelloWorld" splash screen"
//    auto sprite = Sprite::create("HelloWorld.png");
//
//    // position the sprite on the center of the screen
//    sprite->setPosition(Vec2(visibleSize.width/2 + origin.x, visibleSize.height/2 + origin.y));
//
//    // add the sprite as a child to this layer
//    this->addChild(sprite, 0);
    
    auto background = Sprite::create("background.jpeg");
    background->setPosition(Vec2(visibleSize.width/2,visibleSize.height/2));
    background->setContentSize(Size(visibleSize.width,visibleSize.height));
    this->addChild(background);
    
    auto _player = Sprite::create("player.png");
    _player->setPosition(Vec2(visibleSize.width * 0.1, visibleSize.height * 0.5));
    _player->setScale(0.1, 0.1);
    this->addChild(_player);
    
    srand((unsigned int)time(nullptr));
    this->schedule(schedule_selector(HelloWorld::addMonster), 1.5);
    
    return true;
}


void HelloWorld::menuCloseCallback(Ref* pSender)
{
    //Close the cocos2d-x game scene and quit the application
    Director::getInstance()->end();

    #if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
    exit(0);
#endif
    
    /*To navigate back to native iOS screen(if present) without quitting the application  ,do not use Director::getInstance()->end() and exit(0) as given above,instead trigger a custom event created in RootViewController.mm as below*/
    
    //EventCustom customEndEvent("game_scene_close_event");
    //_eventDispatcher->dispatchEvent(&customEndEvent);
    
    
}

void HelloWorld::addMonster(float dt) {
    auto monster = Sprite::create("monster.png");
    
    // 1
    monster->setScale(0.1, 0.1);
    auto monsterContentSize = monster->getContentSize();
    auto selfContentSize = this->getContentSize();
    int minY = monsterContentSize.height/2*0.1;
    int maxY = selfContentSize.height - monsterContentSize.height/2*0.1;
    int rangeY = maxY - minY;
    int randomY = (rand() % rangeY) + minY;

    monster->setPosition(Vec2(selfContentSize.width + monsterContentSize.width/2, randomY));
    
    this->addChild(monster);

    // 2
    int minDuration = 5.0;
    int maxDuration = 10.0;
    int rangeDuration = maxDuration - minDuration;
    int randomDuration = (rand() % rangeDuration) + minDuration;

    // 3
    auto actionMove = MoveTo::create(randomDuration, Vec2(-monsterContentSize.width/2, randomY));
    auto actionRemove = RemoveSelf::create();
    monster->runAction(Sequence::create(actionMove,actionRemove, nullptr));
}

